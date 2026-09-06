package com.wuweibi.bullet.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 使用授权服务器内存中的授权记录解析 opaque access token。
 */
public class OAuth2AuthorizationServiceOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OAuth2AuthorizationService authorizationService;

    public OAuth2AuthorizationServiceOpaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null || authorization.getAccessToken() == null
                || !authorization.getAccessToken().isActive()) {
            throw new OAuth2IntrospectionException("Access token is invalid");
        }

        OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
        Map<String, Object> attributes = new LinkedHashMap<>(authorization.getAccessToken().getClaims());
        attributes.putIfAbsent("active", true);
        attributes.putIfAbsent("client_id", authorization.getRegisteredClientId());
        attributes.putIfAbsent("sub", authorization.getPrincipalName());
        attributes.putIfAbsent("scope", accessToken.getScopes());
        attributes.putIfAbsent("iat", accessToken.getIssuedAt());
        attributes.putIfAbsent("exp", accessToken.getExpiresAt());

        Set<GrantedAuthority> authorities = readAuthorities(attributes.get("authorities"));
        return new DefaultOAuth2AuthenticatedPrincipal(authorization.getPrincipalName(), attributes, authorities);
    }

    private Set<GrantedAuthority> readAuthorities(Object claim) {
        if (claim instanceof Collection<?> values) {
            return values.stream()
                    .map(String::valueOf)
                    .filter(value -> !value.isBlank())
                    .map(value -> (GrantedAuthority) () -> value)
                    .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
        }
        if (claim instanceof String value && !value.isBlank()) {
            return List.of(value.split("\\s+")).stream()
                    .filter(item -> !item.isBlank())
                    .map(item -> (GrantedAuthority) () -> item)
                    .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
        }
        return Set.of();
    }
}
