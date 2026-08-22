package com.wuweibi.bullet.config.security;

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Include terminal information in the authentication key so one account can hold
 * different access tokens across multiple terminals.
 */
public class MultiTerminalAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    private static final String[] TERMINAL_PARAMETER_NAMES = {
            "client_id",
    };

    private final DefaultAuthenticationKeyGenerator delegate = new DefaultAuthenticationKeyGenerator();

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        String terminal = extractTerminal(authentication);
        if (!StringUtils.hasText(terminal)) {
            return delegate.extractKey(authentication);
        }

        Map<String, String> values = new LinkedHashMap<>();
        values.put("client_id", authentication.getOAuth2Request().getClientId());

        String username = authentication.isClientOnly() ? null : authentication.getName();
        if (StringUtils.hasText(username)) {
            values.put("username", username);
        }

        Set<String> scope = authentication.getOAuth2Request().getScope();
        if (scope != null && !scope.isEmpty()) {
            values.put("scope", OAuth2Utils.formatParameterList(scope));
        }
        values.put("terminal", terminal);

        return DigestUtils.md5DigestAsHex(values.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String extractTerminal(OAuth2Authentication authentication) {
        Map<String, String> parameters = authentication.getOAuth2Request().getRequestParameters();
        if (parameters == null || parameters.isEmpty()) {
            return null;
        }
        for (String key : TERMINAL_PARAMETER_NAMES) {
            String value = parameters.get(key);
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return null;
    }
}
