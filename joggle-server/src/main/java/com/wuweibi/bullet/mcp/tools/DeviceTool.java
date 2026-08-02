package com.wuweibi.bullet.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.mcp.McpProtocolHandler;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class DeviceTool {

    @Resource
    private McpProtocolHandler protocolHandler;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private DeviceService deviceService;

    @PostConstruct
    public void init() {
        protocolHandler.registerTool("list_devices", "获取用户的设备列表", inputSchema(), this::listDevices);
    }

    private JsonNode inputSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        schema.putObject("properties");
        schema.putArray("required");
        return schema;
    }

    private JsonNode listDevices(Long userId, JsonNode args) {
        List<DeviceOption> devices = deviceService.getOptionListByUserId(userId);
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode arr = result.putArray("devices");
        for (DeviceOption d : devices) {
            ObjectNode item = arr.addObject();
            item.put("id", d.getId());
            item.put("name", d.getName());
            item.put("deviceNo", d.getDeviceNo());
            item.put("intranetIp", d.getIntranetIp());
            item.put("publicIp", d.getPublicIp());
        }
        return result;
    }
}
