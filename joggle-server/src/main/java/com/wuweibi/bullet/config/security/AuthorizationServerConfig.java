package com.wuweibi.bullet.config.security;

import com.wuweibi.bullet.config.security.oauth2.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.wuweibi.bullet.config.security.oauth2.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

/**
 * Spring Authorization Server 配置。
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {

    private final AuthenticationManager authenticationManager;

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<?> tokenGenerator;

    @Value("${spring.security.oauth2.default-client-id:client_manager}")
    private String defaultClientId;

    @Value("${spring.security.oauth2.default-client-secret:password}")
    private String defaultClientSecret;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.getEndpointsMatcher()))
                .addFilterBefore(new DefaultOAuth2ClientParameterFilter(defaultClientId, defaultClientSecret),
                        SecurityContextHolderFilter.class)
                .with(authorizationServerConfigurer, authorizationServer -> authorizationServer
                        .authorizationService(authorizationService)
                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                .accessTokenRequestConverter(new OAuth2ResourceOwnerPasswordAuthenticationConverter())
                                .authenticationProvider(new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                                        authenticationManager, authorizationService, tokenGenerator)))
                )
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .tokenEndpoint("/oauth/token")
                .authorizationEndpoint("/oauth/authorize")
                .tokenRevocationEndpoint("/oauth/revoke")
                .tokenIntrospectionEndpoint("/oauth/check_token")
                .jwkSetEndpoint("/oauth/jwks")
                .build();
    }
}
