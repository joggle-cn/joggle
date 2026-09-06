package com.wuweibi.bullet.mcp.tools;

import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.mcp.auth.McpSecurityContext;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.stereotype.Component;
import org.springaicommunity.mcp.annotation.McpTool;

import jakarta.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeviceTool {

    @Resource
    private DeviceService deviceService;

    @McpTool(name = "list_devices", description = "获取当前 API Key 用户的设备列表")
    public Map<String, Object> listDevices() {
        Long userId = McpSecurityContext.getRequiredUserId();
        List<DeviceOption> devices = deviceService.getOptionListByUserId(userId);
        return Map.of("devices", devices.stream().map(this::toMap).toList());
    }

    private Map<String, Object> toMap(DeviceOption d) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", d.getId());
        item.put("name", d.getName());
        item.put("deviceNo", d.getDeviceNo());
        item.put("intranetIp", d.getIntranetIp());
        item.put("publicIp", d.getPublicIp());
        return item;
    }
}
