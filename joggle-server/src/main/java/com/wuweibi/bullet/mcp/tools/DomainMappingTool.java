package com.wuweibi.bullet.mcp.tools;

import com.wuweibi.bullet.device.domain.dto.DeviceMappingDomainDTO;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingClientVO;
import com.wuweibi.bullet.device.service.DeviceMappingManagerService;
import com.wuweibi.bullet.device.service.DeviceMappingViewService;
import com.wuweibi.bullet.domain2.domain.vo.DomainOptionVO;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.mcp.auth.McpSecurityContext;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import org.springframework.stereotype.Component;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import jakarta.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DomainMappingTool {

    @Resource
    private DeviceMappingManagerService deviceMappingManagerService;
    @Resource
    private DeviceMappingViewService deviceMappingViewService;
    @Resource
    private DeviceMappingService deviceMappingService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DomainService domainService;

    @McpTool(name = "list_domain_mappings", description = "查看指定设备的域名映射列表")
    public Map<String, Object> listMappings(
            @McpToolParam(description = "设备ID") Long deviceId) {
        Long userId = McpSecurityContext.getRequiredUserId();
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        if (deviceInfo == null || !userId.equals(deviceInfo.getUserId())) {
            return errorResult("设备不存在");
        }
        List<DeviceMappingClientVO> mappings = deviceMappingViewService.getDomainMappings(deviceId, deviceInfo);
        return Map.of("mappings", mappings.stream().map(this::toMap).toList());
    }

    @McpTool(name = "create_domain_mapping", description = "创建域名映射，将域名绑定到设备的内网服务")
    public Map<String, Object> createMapping(
            @McpToolParam(description = "设备ID") Long deviceId,
            @McpToolParam(description = "域名资源ID，通过 get_available_domains 获取") Long domainId,
            @McpToolParam(description = "协议：1=HTTP，3=HTTPS，4=HTTP/HTTPS") Integer protocol,
            @McpToolParam(description = "内网服务IP地址") String host,
            @McpToolParam(description = "内网服务端口") Integer port,
            @McpToolParam(description = "映射名称") String name,
            @McpToolParam(description = "备注说明", required = false) String description) {
        Long userId = McpSecurityContext.getRequiredUserId();
        DeviceMappingDomainDTO dto = new DeviceMappingDomainDTO();
        dto.setDeviceId(deviceId);
        dto.setUserDomainId(domainId);
        dto.setProtocol(protocol);
        dto.setHost(host);
        dto.setPort(port);
        dto.setName(name);
        dto.setDescription(description);
        dto.setStatus(1);

        R<?> result = deviceMappingManagerService.saveDomainMapping(userId, dto);
        if (result.isFail()) {
            return errorResult(result.getMsg());
        }
        return Map.of("success", true, "message", "域名映射创建成功");
    }

    @McpTool(name = "delete_domain_mapping", description = "删除指定的域名映射")
    public Map<String, Object> deleteMapping(
            @McpToolParam(description = "映射ID") Long mappingId) {
        Long userId = McpSecurityContext.getRequiredUserId();
        boolean exists = deviceMappingService.exists(userId, mappingId);
        if (!exists) {
            return errorResult("映射不存在");
        }
        deviceMappingService.removeById(mappingId);
        return Map.of("success", true, "message", "域名映射已删除");
    }

    @McpTool(name = "get_available_domains", description = "获取用户可用的域名资源列表")
    public Map<String, Object> availableDomains(
            @McpToolParam(description = "服务器通道ID") Integer serverTunnelId) {
        Long userId = McpSecurityContext.getRequiredUserId();
        List<DomainOptionVO> domains = domainService.getListNotBindByUserId(userId, serverTunnelId, 2);
        return Map.of("domains", domains.stream().map(this::toMap).toList());
    }

    private Map<String, Object> toMap(DeviceMappingClientVO m) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", m.getId());
        item.put("name", m.getName());
        item.put("domain", m.getDomain());
        item.put("url", m.getUrl());
        item.put("protocol", m.getProtocol());
        item.put("host", m.getHost());
        item.put("port", m.getPort());
        item.put("remotePort", m.getRemotePort());
        item.put("status", m.getStatus());
        item.put("description", m.getDescription());
        return item;
    }

    private Map<String, Object> toMap(DomainOptionVO d) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", d.getId());
        item.put("domainFull", d.getDomainFull());
        item.put("type", d.getType());
        return item;
    }

    private Map<String, Object> errorResult(String msg) {
        return Map.of("_error", true, "message", msg);
    }
}
