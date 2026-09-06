package com.wuweibi.bullet.mcp.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class McpSecurityContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private McpSecurityContext() {
    }

    static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getRequiredUserId() {
        Long userId = USER_ID.get();
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }
        return userId;
    }

    static void clear() {
        USER_ID.remove();
    }
}
