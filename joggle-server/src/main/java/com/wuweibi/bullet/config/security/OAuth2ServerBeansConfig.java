package com.wuweibi.bullet.config.security;

import com.wuweibi.bullet.oauth2.security.UserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.List;

/**
 * OAuth2 授权服务器和资源服务器共用 Bean。
 */
@Configuration
public class OAuth2ServerBeansConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new OauthClientDetailsRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer() {
        return context -> {
            context.getClaims().claim("client_id", context.getRegisteredClient().getClientId());
            if (context.getPrincipal().getPrincipal() instanceof UserDetail user) {
                context.getClaims()
                        .claim("userId", user.getId())
                        .claim("userName", user.getName())
                        .claim("code", "S00")
                        .claim("authorities", user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList());
            } else {
                context.getClaims().claim("authorities", List.of());
            }
        };
    }

    @Bean
    public OAuth2TokenGenerator<?> oauth2TokenGenerator(
            OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        accessTokenGenerator.setAccessTokenCustomizer(accessTokenCustomizer);
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        return new OAuth2AuthorizationServiceOpaqueTokenIntrospector(authorizationService);
    }
}
