package com.wuweibi.bullet.oauth2.handler.event;

import com.wuweibi.bullet.oauth2.security.UserDetail;
import com.wuweibi.bullet.oauth2.service.OauthUserService;
import com.wuweibi.bullet.ratelimiter.util.WebUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


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
        // 用于监控会话
        WebUtils.setSessionAttribute(WebUtils.getRequest() ,"monitor", "true");


//        Authentication authentication = (Authentication) event.getSource();
//        if (isUserAuthentication(authentication)) {
//            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
//                    .getRequestAttributes();
//            HttpServletRequest request = requestAttributes.getRequest();
//            HttpServletResponse response = requestAttributes.getResponse();
            // 登录成功
//            UserDetail loginUserInfo = (UserDetail)authentication.getPrincipal();
            // 更新登录时间
//            oauthUserService.updateLoginTime(loginUserInfo.getId());
//
//        }

    }

    private boolean isUserAuthentication(Authentication authentication) {
        return authentication.getPrincipal() instanceof UserDetail
                || !CollectionUtils.isEmpty(authentication.getAuthorities());
    }

}
