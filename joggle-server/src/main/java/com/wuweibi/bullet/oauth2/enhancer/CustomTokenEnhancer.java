package com.wuweibi.bullet.oauth2.enhancer;

import com.google.common.collect.Maps;
//import org.springframework.security.UserDetail;
import com.wuweibi.bullet.oauth2.security.UserDetail;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Map;

/**
 * 自定义token携带内容
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = Maps.newHashMap();

        UserDetail user = (UserDetail) authentication.getPrincipal();
//
//        //自定义token内容，加入组织机构信息
        additionalInfo.put("userId", user.getId());
        additionalInfo.put("userName", user.getName());
        additionalInfo.put("code", "S00");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}