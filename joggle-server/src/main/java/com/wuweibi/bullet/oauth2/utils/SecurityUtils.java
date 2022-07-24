package com.wuweibi.bullet.oauth2.utils;

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.oauth2.service.AuthenticationService;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 权限工具类
 * @author marker
 */
public final class SecurityUtils {



    public static Session getLoginUser(){
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


    public static Long getUserId(){
        return getLoginUser().getUserId();
    }

    public static void getClienScope() {
        OAuth2Authentication authentication = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        String clientId = authentication.getOAuth2Request().getClientId();


    }
}
