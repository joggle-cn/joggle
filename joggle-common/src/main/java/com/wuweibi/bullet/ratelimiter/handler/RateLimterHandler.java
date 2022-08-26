package com.wuweibi.bullet.ratelimiter.handler;

import com.wuweibi.bullet.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author snowalker
 * @version 1.0
 * @date 2018/10/27 1:17
 * @className RateLimterHandler
 * @desc 限流处理器
 */
@Slf4j
@Aspect
@Component
public class RateLimterHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimterHandler.class);

    @Resource
    RedisTemplate redisTemplate;

    private DefaultRedisScript<Long> getRedisScript;

    @Resource
    private RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        getRedisScript = new DefaultRedisScript<>();
        getRedisScript.setResultType(Long.class);
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimter.lua")));
        LOGGER.info("RateLimterHandler[分布式限流处理器]脚本加载完成");
    }

    @Pointcut("@annotation(com.wuweibi.bullet.ratelimiter.annotation.RateLimiter)")
    public void rateLimiter() {}

    @Around("@annotation(rateLimiter)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, RateLimiter rateLimiter) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RateLimterHandler[分布式限流处理器]开始执行限流操作");
        }
        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("the Annotation @RateLimter must used on method!");
        }



        RRateLimiter limiter = redissonClient.getRateLimiter("rateLimiter");
        if (limiter.tryAcquire()) {//尝试获取1个令牌
            log.debug("{}, 成功获取到令牌", Thread.currentThread().getName());
            return proceedingJoinPoint.proceed();
        } else {
            System.out.println(Thread.currentThread().getName() + "未获取到令牌");
        }
        return null;



        /**
         * 获取注解参数
         */
//        // 限流模块key
//        String limitKey = rateLimiter.key();
//        Preconditions.checkNotNull(limitKey);
//        // 限流阈值
//        long limitTimes = rateLimiter.limit();
//        // 限流超时时间
//        long expireTime = rateLimiter.expire();
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("RateLimterHandler[分布式限流处理器]参数值为-limitTimes={},limitTimeout={}", limitTimes, expireTime);
//        }
//        // 限流提示语
//        String message = rateLimiter.message();
//        if (StringUtils.isBlank(message)) {
//            message = "false";
//        }
//        /**
//         * 执行Lua脚本
//         */
//        List<String> keyList = new ArrayList();
//        // 设置key值为注解中的值
//        keyList.add(limitKey);
//        /**
//         * 调用脚本并执行
//         */
//        Long result = (Long) redisTemplate.execute(getRedisScript, keyList, expireTime, limitTimes);
//        if (result == 0) {
//            String msg = "由于超过单位时间=" + expireTime + "-允许的请求次数=" + limitTimes + "[触发限流]";
//            LOGGER.debug(msg);
//            return message;
//        }
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("RateLimterHandler[分布式限流处理器]限流执行结果-result={},请求[正常]响应", result);
//        }
//        return proceedingJoinPoint.proceed();
    }
}
