package com.wuweibi.bullet.monitor.aspect;

import com.wuweibi.bullet.oauth2.manager.LoginCountService;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;


/**
 *  修复监控面板url地址 兼容https传递
 * @author marker
 */
@Slf4j
@Aspect
@Configuration
public class MonitorBaseUrlFixAspect {

    @Resource
    private LoginCountService loginCountService;

    /**
     * 登录切面
     */
    @Pointcut("execution(public * de.codecentric.boot.admin.server.ui.web.UiController.getBaseUrl(..))")
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
        String schema = ((ServletRequestAttributes) ra).getRequest().getHeader("X-Forwarded-Proto");
        if (StringUtil.isBlank(schema)) {
            schema = ((ServletRequestAttributes) ra).getRequest().getScheme();
        }
        UriComponentsBuilder uriComponentsBuilder = (UriComponentsBuilder) pjp.getArgs()[0];
        uriComponentsBuilder.scheme(schema);
        return pjp.proceed(pjp.getArgs());
    }

}
