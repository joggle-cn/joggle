package com.wuweibi.bullet.common.lock.core;

import com.wuweibi.bullet.common.lock.annotation.DLock;
import com.wuweibi.bullet.common.lock.core.exception.DlockInvocationException;
import com.wuweibi.bullet.common.lock.lock.Lock;
import com.wuweibi.bullet.common.lock.lock.LockFactory;
import com.wuweibi.bullet.common.lock.model.LockInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aspect 一定是在事务的切面之前执行，如果在事务之后，会导致并发请求的读取状态相同。
 *
 * Created by kl on 2017/12/29.
 * Content :给添加@DLock切面加锁处理
 */
@Aspect
@Component
@Order(0)
public class DLockAspectHandler {

    private static final Logger logger = LoggerFactory.getLogger(DLockAspectHandler.class);

    @Resource
    LockFactory lockFactory;

    @Resource
    private LockInfoProvider lockInfoProvider;

    private final Map<String,LockRes> currentThreadLock = new ConcurrentHashMap<>();


    @Around(value = "@annotation(DLock)")
    public Object around(ProceedingJoinPoint joinPoint, DLock DLock) throws Throwable {
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, DLock);
        String curentLock = this.getCurrentLockId(joinPoint, DLock);
        currentThreadLock.put(curentLock,new LockRes(lockInfo, false));
        Lock lock = lockFactory.getLock(lockInfo);
        boolean lockRes = lock.acquire();

        //如果获取锁失败了，则进入失败的处理逻辑
        if(!lockRes) {
            if(logger.isWarnEnabled()) {
                logger.warn("Timeout while acquiring Lock({})", lockInfo.getName());
            }
            //如果自定义了获取锁失败的处理策略，则执行自定义的降级处理策略
            if(!StringUtils.isEmpty(DLock.customLockTimeoutStrategy())) {

                return handleCustomLockTimeout(DLock.customLockTimeoutStrategy(), joinPoint);

            } else {
                //否则执行预定义的执行策略
                //注意：如果没有指定预定义的策略，默认的策略为静默啥不做处理
                DLock.lockTimeoutStrategy().handle(lockInfo, lock, joinPoint);
            }
        }

        currentThreadLock.get(curentLock).setLock(lock);
        currentThreadLock.get(curentLock).setRes(true);

        Object obj = joinPoint.proceed();

        return obj;
    }


    @AfterReturning(value = "@annotation(DLock)")
    public void afterReturning(JoinPoint joinPoint, DLock DLock) throws Throwable {
        String curentLock = this.getCurrentLockId(joinPoint, DLock);
        releaseLock(DLock, joinPoint,curentLock);
        cleanUpThreadLocal(curentLock);
    }

    @AfterThrowing(value = "@annotation(DLock)", throwing = "ex")
    public void afterThrowing (JoinPoint joinPoint, DLock DLock, Throwable ex) throws Throwable {
        String curentLock = this.getCurrentLockId(joinPoint, DLock);
        releaseLock(DLock, joinPoint,curentLock);
        cleanUpThreadLocal(curentLock);
        throw ex;
    }

    /**
     * 处理自定义加锁超时
     */
    private Object handleCustomLockTimeout(String lockTimeoutHandler, JoinPoint joinPoint) throws Throwable {

        // prepare invocation context
        Method currentMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Method handleMethod = null;
        try {
            handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(lockTimeoutHandler, currentMethod.getParameterTypes());
            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Illegal annotation param customLockTimeoutStrategy",e);
        }
        Object[] args = joinPoint.getArgs();

        // invoke
        Object res = null;
        try {
            res = handleMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new DlockInvocationException("Fail to invoke custom lock timeout handler: " + lockTimeoutHandler );
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

        return res;
    }

    /**
     *  释放锁
     */
    private void releaseLock(DLock DLock, JoinPoint joinPoint, String curentLock) throws Throwable {
        LockRes lockRes = currentThreadLock.get(curentLock);
        if(Objects.isNull(lockRes)){
            throw new NullPointerException("Please check whether the input parameter used as the lock key value has been modified in the method, which will cause the acquire and release locks to have different key values and throw null pointers.curentLockKey:" + curentLock);
        }
        if (lockRes.getRes()) {
            boolean releaseRes = currentThreadLock.get(curentLock).getLock().release();
            // avoid release lock twice when exception happens below
            lockRes.setRes(false);
            if (!releaseRes) {
                handleReleaseTimeout(DLock, lockRes.getLockInfo(), joinPoint);
            }
        }
    }

    // avoid memory leak
    private void cleanUpThreadLocal(String curentLock) {
        currentThreadLock.remove(curentLock);
    }

    /**
     * 获取当前锁在map中的key
     * @param joinPoint
     * @param DLock
     * @return
     */
    private String getCurrentLockId(JoinPoint joinPoint , DLock DLock){
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, DLock);
        String curentLock= Thread.currentThread().getId() + lockInfo.getName();
        return curentLock;
    }

    /**
     *  处理释放锁时已超时
     */
    private void handleReleaseTimeout(DLock DLock, LockInfo lockInfo, JoinPoint joinPoint) throws Throwable {

        if(logger.isWarnEnabled()) {
            logger.warn("Timeout while release Lock({})", lockInfo.getName());
        }

        if(!StringUtils.isEmpty(DLock.customReleaseTimeoutStrategy())) {

            handleCustomReleaseTimeout(DLock.customReleaseTimeoutStrategy(), joinPoint);

        } else {
            DLock.releaseTimeoutStrategy().handle(lockInfo);
        }

    }

    /**
     * 处理自定义释放锁时已超时
     */
    private void handleCustomReleaseTimeout(String releaseTimeoutHandler, JoinPoint joinPoint) throws Throwable {

        Method currentMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Method handleMethod = null;
        try {
            handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(releaseTimeoutHandler, currentMethod.getParameterTypes());
            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Illegal annotation param customReleaseTimeoutStrategy",e);
        }
        Object[] args = joinPoint.getArgs();

        try {
            handleMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new DlockInvocationException("Fail to invoke custom release timeout handler: " + releaseTimeoutHandler);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private class LockRes {

        private LockInfo lockInfo;
        private Lock lock;
        private Boolean res;

        LockRes(LockInfo lockInfo, Boolean res) {
            this.lockInfo = lockInfo;
            this.res = res;
        }

        LockInfo getLockInfo() {
            return lockInfo;
        }

        public Lock getLock() {
            return lock;
        }

        public void setLock(Lock lock) {
            this.lock = lock;
        }

        Boolean getRes() {
            return res;
        }

        void setRes(Boolean res) {
            this.res = res;
        }

        void setLockInfo(LockInfo lockInfo) {
            this.lockInfo = lockInfo;
        }
    }


}
