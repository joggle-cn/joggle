package com.wuweibi.bullet.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 兼容旧登录接口未显式传 OAuth2 客户端凭据的调用方式。
 */
public class DefaultOAuth2ClientParameterFilter extends OncePerRequestFilter {

    private final String defaultClientId;

    private final String defaultClientSecret;

    public DefaultOAuth2ClientParameterFilter(String defaultClientId, String defaultClientSecret) {
        this.defaultClientId = defaultClientId;
        this.defaultClientSecret = defaultClientSecret;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"/oauth/token".equals(request.getServletPath())
                || !"POST".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.startsWithIgnoreCase(authorization, "Basic ")
                || (StringUtils.isNotBlank(request.getParameter(OAuth2ParameterNames.CLIENT_ID))
                && StringUtils.isNotBlank(request.getParameter(OAuth2ParameterNames.CLIENT_SECRET)))) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientId = StringUtils.defaultIfBlank(request.getParameter(OAuth2ParameterNames.CLIENT_ID), defaultClientId);
        String clientSecret = StringUtils.defaultIfBlank(request.getParameter(OAuth2ParameterNames.CLIENT_SECRET), defaultClientSecret);
        filterChain.doFilter(new OAuth2ClientParameterRequestWrapper(request, clientId, clientSecret),
                response);
    }

    private static final class OAuth2ClientParameterRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, String[]> parameters;

        private OAuth2ClientParameterRequestWrapper(HttpServletRequest request, String clientId, String clientSecret) {
            super(request);
            this.parameters = new LinkedHashMap<>(request.getParameterMap());
            this.parameters.put(OAuth2ParameterNames.CLIENT_ID, new String[]{clientId});
            this.parameters.put(OAuth2ParameterNames.CLIENT_SECRET, new String[]{clientSecret});
        }

        @Override
        public String getParameter(String name) {
            String[] values = parameters.get(name);
            return values == null || values.length == 0 ? null : values[0];
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(parameters);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameters.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return parameters.get(name);
        }
    }
}
