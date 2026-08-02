package com.wuweibi.bullet.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wuweibi.bullet.device.domain.vo.TunnelOption;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.mcp.McpProtocolHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class ServerTunnelTool {

    @Resource
    private McpProtocolHandler protocolHandler;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ServerTunnelService serverTunnelService;

    @PostConstruct
    public void init() {
        protocolHandler.registerTool("list_server_tunnels", "获取服务器通道列表",
                inputSchema(), this::listTunnels);
    }

    private JsonNode inputSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        schema.putObject("properties");
        schema.putArray("required");
        return schema;
    }

    private JsonNode listTunnels(Long userId, JsonNode args) {
        List<TunnelOption> tunnels = serverTunnelService.getOptionList();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode arr = result.putArray("tunnels");
        for (TunnelOption t : tunnels) {
            ObjectNode item = arr.addObject();
            item.put("id", t.getId());
            item.put("name", t.getName());
        }
        return result;
    }
}
