package com.wuweibi.bullet.config.security.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

public class OAuth2ResourceOwnerPasswordAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

    public static final AuthorizationGrantType PASSWORD_GRANT_TYPE = new AuthorizationGrantType("password");

    public OAuth2ResourceOwnerPasswordAuthenticationToken(Authentication clientPrincipal, Set<String> scopes,
                                                         Map<String, Object> additionalParameters) {
        super(PASSWORD_GRANT_TYPE, clientPrincipal, scopes, additionalParameters);
    }
}
