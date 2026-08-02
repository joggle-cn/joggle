package com.wuweibi.bullet.mcp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wuweibi.bullet.mcp.McpProtocolHandler;
import com.wuweibi.bullet.mcp.McpSessionManager;
import com.wuweibi.bullet.mcp.auth.McpAuthenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/open/mcp")
public class McpController {

    @Resource
    private McpProtocolHandler protocolHandler;
    @Resource
    private McpAuthenticator authenticator;
    @Resource
    private McpSessionManager sessionManager;
    @Resource
    private ObjectMapper objectMapper;

    @PostMapping("/message")
    public ResponseEntity<JsonNode> handleMessage(
            @RequestHeader(value = "X-API-Key", required = false) String apiKeyHeader,
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestBody(required = false) JsonNode requestBody) {
        if (requestBody == null) {
            return ResponseEntity.ok(errorResponse(null, -32700, "Parse error: empty request body"));
        }
        String reqId = requestBody.has("id") ? requestBody.get("id").asText() : null;

        Long userId = authenticate(   apiKeyHeader   );
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }

        JsonNode response = protocolHandler.handleMessage(requestBody, userId);
        if (response == null) {
            return ResponseEntity.accepted().build();
        }
        if (sessionId != null && !sessionId.isBlank()) {
            sessionManager.send(sessionId, response);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.ok(response);
    }

    private Long authenticate(  String apiKeyHeader ) {
        String apiKey = null;
        if (apiKeyHeader != null && !apiKeyHeader.isBlank()) {
            apiKey = apiKeyHeader.trim();
        }
        if (apiKey == null) {
            return null;
        }
        return authenticator.authenticate(apiKey);
    }

    private JsonNode errorResponse(String id, int code, String message) {
        ObjectNode resp = objectMapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) {
            resp.put("id", id);
        } else {
            resp.putNull("id");
        }
        resp.putObject("error").put("code", code).put("message", message);
        return resp;
    }
}
