package com.wuweibi.bullet.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class McpProtocolHandler {

    private static final String PROTOCOL_VERSION = "2025-03-26";

    @Resource
    private ObjectMapper objectMapper;

    private final Map<String, ToolEntry> tools = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
    }

    public void registerTool(String name, String description, JsonNode inputSchema,
                             BiFunction<Long, JsonNode, JsonNode> executor) {
        McpToolDefinition def = new McpToolDefinition();
        def.setName(name);
        def.setDescription(description);
        def.setInputSchema(inputSchema);
        tools.put(name, new ToolEntry(def, executor));
    }

    public JsonNode handleMessage(JsonNode request, Long userId) {
        String method = request.has("method") ? request.get("method").asText() : "";
        String id = request.has("id") ? request.get("id").asText() : null;

        try {
            switch (method) {
                case "initialize":
                    return handleInitialize(id, request);
                case "ping":
                    return handlePing(id);
                case "tools/list":
                    return handleToolsList(id);
                case "tools/call":
                    return handleToolsCall(id, request, userId);
                case "notifications/initialized":
                case "notifications/cancelled":
                case "notifications/roots/list_changed":
                case "notifications/resources/updated":
                case "notifications/resources/list_changed":
                case "notifications/prompts/list_changed":
                case "notifications/tools/list_changed":
                    return null;
                default:
                    if (method.startsWith("notifications/")) {
                        return null;
                    }
                    return error(id, -32601, "Method not found: " + method);
            }
        } catch (Exception e) {
            return error(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    private JsonNode handleInitialize(String id, JsonNode request) {
        ObjectNode result = objectMapper.createObjectNode();
        result.put("protocolVersion", PROTOCOL_VERSION);
        result.putObject("capabilities").putObject("tools");
        result.putObject("serverInfo").put("name", "joggle-mcp-server").put("version", "1.0.0");
        return success(id, result);
    }

    private JsonNode handlePing(String id) {
        return success(id, objectMapper.createObjectNode());
    }

    private JsonNode handleToolsList(String id) {
        ArrayNode toolsArray = objectMapper.createArrayNode();
        for (ToolEntry entry : tools.values()) {
            ObjectNode toolNode = objectMapper.createObjectNode();
            toolNode.put("name", entry.definition.getName());
            toolNode.put("description", entry.definition.getDescription());
            toolNode.set("inputSchema", entry.definition.getInputSchema());
            toolsArray.add(toolNode);
        }
        ObjectNode result = objectMapper.createObjectNode();
        result.set("tools", toolsArray);
        return success(id, result);
    }

    private JsonNode handleToolsCall(String id, JsonNode request, Long userId) {
        String toolName = request.path("params").path("name").asText();
        JsonNode args = request.path("params").path("arguments");

        ToolEntry entry = tools.get(toolName);
        if (entry == null) {
            return error(id, -32602, "Unknown tool: " + toolName);
        }

        JsonNode content = entry.executor.apply(userId, args);
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode contentArray = objectMapper.createArrayNode();
        String text;
        if (content.has("_error") && content.get("_error").asBoolean()) {
            text = content.has("message") ? content.get("message").asText() : "Unknown error";
        } else {
            text = content.toString();
        }
        ObjectNode textContent = contentArray.addObject();
        textContent.put("type", "text");
        textContent.put("text", text);
        result.set("content", contentArray);
        result.put("isError", false);
        return success(id, result);
    }

    private JsonNode success(String id, JsonNode result) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) {
            response.put("id", id);
        } else {
            response.putNull("id");
        }
        response.set("result", result);
        return response;
    }

    private JsonNode error(String id, int code, String message) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) {
            response.put("id", id);
        } else {
            response.putNull("id");
        }
        ObjectNode errorObj = response.putObject("error");
        errorObj.put("code", code);
        errorObj.put("message", message);
        return response;
    }

    public List<McpToolDefinition> getToolDefinitions() {
        List<McpToolDefinition> list = new ArrayList<>();
        for (ToolEntry entry : tools.values()) {
            list.add(entry.definition);
        }
        return list;
    }

    private static class ToolEntry {
        final McpToolDefinition definition;
        final BiFunction<Long, JsonNode, JsonNode> executor;

        ToolEntry(McpToolDefinition definition, BiFunction<Long, JsonNode, JsonNode> executor) {
            this.definition = definition;
            this.executor = executor;
        }
    }
}
