package com.wuweibi.bullet.ratelimiter.filter;

import com.wuweibi.bullet.ratelimiter.properties.RateLimiterProperties;
import com.wuweibi.bullet.ratelimiter.util.RateSpringUtils;
import com.wuweibi.bullet.ratelimiter.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 请求参数过滤器
 * 输出请求参数，支持form表单以及body json
 *
 * @author marker
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimiterFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip  = WebUtils.getIP(request);
        if (WebUtils.isLocal(ip)) {
            filterChain.doFilter(request, response);
            return;
        }
        String key = String.format("limiter:%s", ip);

        RedissonClient redissonClient = RateSpringUtils.getBean(RedissonClient.class);
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        boolean isEx = limiter.isExists();
        if (!isEx) {
            RateLimiterProperties properties = RateSpringUtils.getBean(RateLimiterProperties.class);
            limiter.trySetRate(properties.getType(), properties.getRate(), properties.getInterval(), RateIntervalUnit.MILLISECONDS);//每1秒产生rate个令牌
        }
        if (limiter.tryAcquire()) {//尝试获取1个令牌
            log.debug("{}, 成功获取到令牌", Thread.currentThread().getName());
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("{}, 获取令牌失败", Thread.currentThread().getName());
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.getWriter().write("{\"code\":\"000\", \"msg\":\"are you ok!\"}");
    }


}
