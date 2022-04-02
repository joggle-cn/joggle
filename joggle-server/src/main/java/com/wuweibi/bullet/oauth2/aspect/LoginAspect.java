package com.wuweibi.bullet.oauth2.aspect;

import com.wuweibi.bullet.oauth2.exception.LoginException;
import com.wuweibi.bullet.oauth2.manager.LoginCountService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;


/**
 * 账号登录错误次数限制切面
 *
 * @author marker
 */
@Slf4j
@Aspect
@Configuration
public class LoginAspect {

    @Resource
    private LoginCountService loginCountService;

    /**
     * 登录切面
     */
    @Pointcut("execution(public * com.wuweibi.bullet.oauth2.service.impl.CustomUserDetailsService.loadUserByUsername(..))")
    public void excudeService() {
    }


    /**
     * 处理登录错误限制中
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        String userName = (String) pjp.getArgs()[0];

        // 校验登录次数
        int count = loginCountService.getCount(userName);
        int totalCount = loginCountService.getLoginTotalCount();
        int waitedTime = loginCountService.getWaitedTime(userName); // 剩余等待时长

        if (count >= totalCount) {
            String tip = "请重试";
            if (waitedTime >= 60) {
                tip = "请等待" + waitedTime / 60 + "分钟再试";
            } else {
                if (waitedTime != 0) {
                    tip = "请等待" + waitedTime + "秒再试";
                }
            }
            String msg = String.format("登录失败次数超过%d次, %s",
                    totalCount, tip);
            throw new LoginException(msg);
        }

        return pjp.proceed(pjp.getArgs());
    }

}
