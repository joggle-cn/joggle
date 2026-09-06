package com.wuweibi.bullet.mcp.tools;

import com.wuweibi.bullet.device.domain.vo.TunnelOption;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.mcp.auth.McpSecurityContext;
import org.springframework.stereotype.Component;
import org.springaicommunity.mcp.annotation.McpTool;

import jakarta.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServerTunnelTool {

    @Resource
    private ServerTunnelService serverTunnelService;

    @McpTool(name = "list_server_tunnels", description = "获取服务器通道列表")
    public Map<String, Object> listTunnels() {
        McpSecurityContext.getRequiredUserId();
        List<TunnelOption> tunnels = serverTunnelService.getOptionList();
        return Map.of("tunnels", tunnels.stream().map(this::toMap).toList());
    }

    private Map<String, Object> toMap(TunnelOption t) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", t.getId());
        item.put("name", t.getName());
        return item;
    }
}
