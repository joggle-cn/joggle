package com.wuweibi.bullet.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class McpSessionManager {

    private final Map<String, SseEmitter> sessions = new ConcurrentHashMap<>();

    public String createSession(SseEmitter emitter) {
        String sessionId = UUID.randomUUID().toString().replace("-", "");
        sessions.put(sessionId, emitter);
        emitter.onCompletion(() -> sessions.remove(sessionId));
        emitter.onTimeout(() -> sessions.remove(sessionId));
        emitter.onError(e -> sessions.remove(sessionId));
        return sessionId;
    }

    public boolean send(String sessionId, JsonNode message) {
        SseEmitter emitter = sessions.get(sessionId);
        if (emitter == null || message == null) {
            return false;
        }
        try {
            emitter.send(SseEmitter.event().name("message").data(message.toString()));
            return true;
        } catch (IOException e) {
            sessions.remove(sessionId);
            return false;
        }
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
