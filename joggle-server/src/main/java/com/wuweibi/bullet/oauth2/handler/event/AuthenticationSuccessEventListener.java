package com.wuweibi.bullet.oauth2.handler.event;

import com.wuweibi.bullet.oauth2.security.UserDetail;
import com.wuweibi.bullet.oauth2.service.OauthUserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 认证成功监听
 *
 * @author marker
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Resource
    private OauthUserService oauthUserService;



    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        Authentication authentication = (Authentication) event.getSource();
        if (isUserAuthentication(authentication)) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            HttpServletResponse response = requestAttributes.getResponse();
            // 登录成功
            UserDetail loginUserInfo = (UserDetail)authentication.getPrincipal();
            // 更新登录时间
            oauthUserService.updateLoginTime(loginUserInfo.getId());

        }

    }

    private boolean isUserAuthentication(Authentication authentication) {
        return authentication.getPrincipal() instanceof UserDetail
                || !CollectionUtils.isEmpty(authentication.getAuthorities());
    }

}