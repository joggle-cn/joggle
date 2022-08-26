package com.wuweibi.bullet.ratelimiter.handler;

import com.wuweibi.bullet.ratelimiter.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
@Slf4j
public class MyInterceptor implements HandlerInterceptor {

    @Resource
    private RedissonClient redissonClient;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = WebUtils.getIP(request);
        String key = String.format("limiter:%s", ip);
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        limiter.trySetRate(RateType.PER_CLIENT, 1, 1, RateIntervalUnit.SECONDS);//每1秒产生5个令牌

        if (limiter.tryAcquire()) {//尝试获取1个令牌
            log.debug("{}, 成功获取到令牌", Thread.currentThread().getName());
            return true;
        }
        log.debug("{}, 获取到令牌失败", Thread.currentThread().getName());
        return false;
    }

}
