package com.wuweibi.bullet.oauth2.handler;
/**
 * Created by marker on 2018/5/28.
 */

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.oauth2.service.AuthenticationService;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.jwt.Jwt;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;


/**
 * Jwt User参数解析器
 *
 * @author marker
 * @create 2018-05-28 14:08
 **/
@Slf4j
@ControllerAdvice
public class JwtUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(JwtUser.class)) {
            log.debug("JwtUserHandlerMethodArgumentResolver");
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
//        JwtUser currentUserAnnotation = parameter.getParameterAnnotation(JwtUser.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);

        AuthenticationService authenticationService =  SpringUtils.getBean(AuthenticationService.class);
        String claims = null;
        if(StringUtil.isNotBlank(authentication)) {
            Jwt jwt = authenticationService.getJwt(authentication);
            claims = jwt.getClaims();
        }
        // 获取如果为空设置默认值
        String sessionJsonStr =  StringUtils.defaultIfBlank(claims, "{}");
        // 转换为对象
        Session sessionUser = JSON.parseObject(sessionJsonStr, Session.class);

        return sessionUser;
    }
}
