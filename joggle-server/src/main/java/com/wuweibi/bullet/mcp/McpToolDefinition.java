package com.wuweibi.bullet.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class McpToolDefinition {

    private String name;
    private String description;
    private JsonNode inputSchema;
}
