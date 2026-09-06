package com.wuweibi.bullet.mcp.auth;

import com.wuweibi.bullet.system.api_key.service.UserApiKeyService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class McpAuthenticator {

    @Resource
    private UserApiKeyService userApiKeyService;

    /**
     * 验证 API Key，返回用户ID
     */
    public Long authenticate(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }
        return userApiKeyService.authenticate(apiKey);
    }
}
