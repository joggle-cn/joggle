package com.wuweibi.bullet.mcp.controller;

import com.wuweibi.bullet.mcp.McpSessionManager;
import com.wuweibi.bullet.mcp.auth.McpAuthenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/api/open/mcp")
public class McpSseController {

    @Resource
    private McpSessionManager sessionManager;
    @Resource
    private McpAuthenticator authenticator;

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestHeader(value = "X-API-Key", required = false) String apiKeyHeader,
            @RequestParam(value = "token", required = false) String token) {
        Long userId = authenticate(auth, apiKeyHeader, token);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }

        SseEmitter emitter = new SseEmitter(0L);
        String sessionId = sessionManager.createSession(emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("endpoint")
                    .data("/api/open/mcp/message?sessionId=" + sessionId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    private Long authenticate(String auth, String apiKeyHeader, String token) {
        String apiKey = null;
        if (apiKeyHeader != null && !apiKeyHeader.isBlank()) {
            apiKey = apiKeyHeader.trim();
        } else if (auth != null && auth.startsWith("Bearer ")) {
            apiKey = auth.substring(7).trim();
        } else if (token != null && !token.isBlank()) {
            apiKey = token;
        }
        if (apiKey == null) {
            return null;
        }
        return authenticator.authenticate(apiKey);
    }
}
