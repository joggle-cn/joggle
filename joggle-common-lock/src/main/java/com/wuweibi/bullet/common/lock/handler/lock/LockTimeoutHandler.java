package com.wuweibi.bullet.common.lock.handler.lock;

import com.wuweibi.bullet.common.lock.lock.Lock;
import com.wuweibi.bullet.common.lock.model.LockInfo;
import org.aspectj.lang.JoinPoint;

/**
 * 获取锁超时的处理逻辑接口
 *
 * @author wanglaomo
 * @since 2019/4/15
 **/
public interface LockTimeoutHandler {

    void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);
}
