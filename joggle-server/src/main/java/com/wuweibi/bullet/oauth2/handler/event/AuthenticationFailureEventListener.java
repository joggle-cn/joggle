package com.wuweibi.bullet.oauth2.handler.event;

import com.wuweibi.bullet.oauth2.manager.LoginCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {


    @Resource
    private LoginCountService loginCountService;

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {

        Authentication authentication = (Authentication) event.getSource();
        String userName = authentication.getName();

        // 登录失败次数+1
        loginCountService.countPlus1(userName);
        int count = loginCountService.getRemainLoginCount(userName);
        log.warn("{}登录失败，还可登录{}次", userName, count);

    }


}
