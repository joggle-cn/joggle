package com.wuweibi.bullet.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDomainDTO;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingClientVO;
import com.wuweibi.bullet.device.service.DeviceMappingManagerService;
import com.wuweibi.bullet.device.service.DeviceMappingViewService;
import com.wuweibi.bullet.domain2.domain.vo.DomainOptionVO;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.mcp.McpProtocolHandler;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class DomainMappingTool {

    @Resource
    private McpProtocolHandler protocolHandler;
    @Resource
    private ObjectMapper objectMapper;
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

    @PostConstruct
    public void init() {
        protocolHandler.registerTool("list_domain_mappings", "查看指定设备的域名映射列表",
                listSchema(), this::listMappings);
        protocolHandler.registerTool("create_domain_mapping", "创建域名映射，将域名绑定到设备的内网服务",
                createSchema(), this::createMapping);
        protocolHandler.registerTool("delete_domain_mapping", "删除指定的域名映射",
                deleteSchema(), this::deleteMapping);
        protocolHandler.registerTool("get_available_domains", "获取用户可用的域名资源列表",
                availableSchema(), this::availableDomains);
    }

    private JsonNode listSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");
        props.putObject("deviceId").put("type", "number").put("description", "设备ID");
        schema.putArray("required").add("deviceId");
        return schema;
    }

    private JsonNode createSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");
        props.putObject("deviceId").put("type", "number").put("description", "设备ID");
        props.putObject("domainId").put("type", "number").put("description", "域名资源ID（通过 get_available_domains 获取）");
        ArrayNode enumArr = props.putObject("protocol").put("type", "number").put("description", "协议 1=HTTP 2=TCP 3=HTTPS 4=HTTP/HTTPS")
                .putArray("enum");
        enumArr.add(1).add(2).add(3).add(4);
        props.putObject("host").put("type", "string").put("description", "内网服务IP地址");
        props.putObject("port").put("type", "number").put("description", "内网服务端口");
        props.putObject("name").put("type", "string").put("description", "映射名称");
        props.putObject("description").put("type", "string").put("description", "备注说明");
        ArrayNode required = schema.putArray("required");
        required.add("deviceId").add("domainId").add("protocol").add("host").add("port").add("name");
        return schema;
    }

    private JsonNode deleteSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");
        props.putObject("mappingId").put("type", "number").put("description", "映射ID");
        schema.putArray("required").add("mappingId");
        return schema;
    }

    private JsonNode availableSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");
        props.putObject("serverTunnelId").put("type", "number").put("description", "服务器通道ID");
        schema.putArray("required").add("serverTunnelId");
        return schema;
    }

    private JsonNode listMappings(Long userId, JsonNode args) {
        long deviceId = args.get("deviceId").asLong();
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        if (deviceInfo == null || !userId.equals(deviceInfo.getUserId())) {
            return errorResult("设备不存在");
        }
        List<DeviceMappingClientVO> mappings = deviceMappingViewService.getDomainMappings(deviceId, deviceInfo);
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode arr = result.putArray("mappings");
        for (DeviceMappingClientVO m : mappings) {
            ObjectNode item = arr.addObject();
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
        }
        return result;
    }

    private JsonNode createMapping(Long userId, JsonNode args) {
        DeviceMappingDomainDTO dto = new DeviceMappingDomainDTO();
        dto.setDeviceId(args.get("deviceId").asLong());
        dto.setProtocol(args.get("protocol").asInt());
        dto.setHost(args.get("host").asText());
        dto.setPort(args.get("port").asInt());
        dto.setName(args.get("name").asText());
        if (args.has("description") && !args.get("description").isNull()) {
            dto.setDescription(args.get("description").asText());
        }
        dto.setStatus(1);

        R<?> result = deviceMappingManagerService.saveDomainMapping(userId, dto);
        if (result.isFail()) {
            return errorResult(result.getMsg());
        }
        ObjectNode resp = objectMapper.createObjectNode();
        resp.put("success", true);
        resp.put("message", "域名映射创建成功");
        return resp;
    }

    private JsonNode deleteMapping(Long userId, JsonNode args) {
        long mappingId = args.get("mappingId").asLong();

        boolean exists = deviceMappingService.exists(userId, mappingId);
        if (!exists) {
            return errorResult("映射不存在");
        }
        deviceMappingService.removeById(mappingId);
        ObjectNode resp = objectMapper.createObjectNode();
        resp.put("success", true);
        resp.put("message", "域名映射已删除");
        return resp;
    }

    private JsonNode availableDomains(Long userId, JsonNode args) {
        int serverTunnelId = args.get("serverTunnelId").asInt();
        List<DomainOptionVO> domains = domainService.getListNotBindByUserId(userId, serverTunnelId, 2);
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode arr = result.putArray("domains");
        for (DomainOptionVO d : domains) {
            ObjectNode item = arr.addObject();
            item.put("id", d.getId());
            item.put("domainFull", d.getDomainFull());
            item.put("type", d.getType());
        }
        return result;
    }

    private JsonNode errorResult(String msg) {
        ObjectNode resp = objectMapper.createObjectNode();
        resp.put("_error", true);
        resp.put("message", msg);
        return resp;
    }
}
