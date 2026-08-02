package com.wuweibi.bullet.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;

import javax.servlet.http.HttpServletRequest;

/**
 * MCP 认证使用自定义头 X-API-Key，不携带 Authorization，OAuth2 的 BearerTokenExtractor 提取不到 token。
 * 该 TokenExtractor 对 /api/open/mcp/** 路径做双重保险：即使客户端带了 Authorization: Bearer，也跳过 OAuth2 token 提取。
 */
public class McpTokenExtractor implements TokenExtractor {

    private static final String MCP_PATH = "/api/open/mcp";

    private final TokenExtractor delegate = new BearerTokenExtractor();

    @Override
    public Authentication extract(HttpServletRequest request) {
        if (request.getRequestURI().startsWith(MCP_PATH)) {
            return null;
        }
        return delegate.extract(request);
    }
}
