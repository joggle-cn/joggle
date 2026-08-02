package com.wuweibi.bullet.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wuweibi.bullet.business.DomainBiz;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDomainDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingPortDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingProtocol;
import com.wuweibi.bullet.device.service.DeviceMappingManagerService;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.domain2.entity.UserDomain;
import com.wuweibi.bullet.domain2.mapper.DomainMapper;
import com.wuweibi.bullet.domain2.service.UserDomainService;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DeviceMappingManagerServiceImpl implements DeviceMappingManagerService {

    private static final List<Integer> DOMAIN_PROTOCOLS = Arrays.asList(
            DeviceMapping.PROTOCOL_HTTP,
            DeviceMapping.PROTOCOL_HTTPS,
            DeviceMapping.PROTOCOL_HTTP_HTTPS
    );

    private static final List<Integer> PORT_PROTOCOLS = Arrays.asList(
            DeviceMapping.PROTOCOL_TCP,
            DeviceMapping.PROTOCOL_UDP
    );

    @Resource
    private DeviceMappingService deviceMappingService;
    @Resource
    private DomainMapper domainMapper;
    @Resource
    private DeviceService deviceService;
    @Resource
    private UserFlowService userFlowService;
    @Resource
    private UserDomainService userDomainService;
    @Resource
    private DomainService domainService;
    @Resource
    private DomainBiz domainBiz;
    @Resource
    private WebsocketPool coonPool;

    @Override
    public R saveDomainMapping(Long userId, DeviceMappingDomainDTO dto) {
        if (!DOMAIN_PROTOCOLS.contains(dto.getProtocol())) {
            return R.fail("域名映射协议不合法");
        }

        DeviceMapping entity = getOrCreate(dto.getId());
        if (Objects.isNull(entity.getDeviceId())) {
            entity.setDeviceId(dto.getDeviceId());
        }
        entity.setProtocol(dto.getProtocol());
        entity.setPortProtocol(toPortProtocol(dto.getProtocol()));
        entity.setPort(dto.getPort());
        entity.setHost(dto.getHost());
        entity.setUserDomainId(dto.getUserDomainId());
        entity.setStatus(dto.getStatus());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        return saveOrUpdateMapping(userId, entity);
    }

    @Override
    public R savePortMapping(Long userId, DeviceMappingPortDTO dto) {
        if (!PORT_PROTOCOLS.contains(dto.getProtocol())) {
            return R.fail("端口映射协议不合法");
        }

        DeviceMapping entity = getOrCreate(dto.getId());
        if (Objects.isNull(entity.getDeviceId())) {
            entity.setDeviceId(dto.getDeviceId());
        }
        entity.setProtocol(dto.getProtocol());
        entity.setPortProtocol(toPortProtocol(dto.getProtocol()));
        entity.setHost(dto.getHost());
        entity.setPort(dto.getPort());
        entity.setStatus(dto.getStatus());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        return saveOrUpdateMapping(userId, entity);
    }

    @Override
    public List<DeviceMapping> listDomainMappings(Long userId, Long deviceId) {
        return deviceMappingService.list(Wrappers.<DeviceMapping>lambdaQuery()
                .eq(DeviceMapping::getUserId, userId)
                .eq(DeviceMapping::getDeviceId, deviceId)
                .in(DeviceMapping::getProtocol, DOMAIN_PROTOCOLS)
                .orderByDesc(DeviceMapping::getId));
    }

    @Override
    public List<DeviceMapping> listPortMappings(Long userId, Long deviceId) {
        return deviceMappingService.list(Wrappers.<DeviceMapping>lambdaQuery()
                .eq(DeviceMapping::getUserId, userId)
                .eq(DeviceMapping::getDeviceId, deviceId)
                .in(DeviceMapping::getProtocol, PORT_PROTOCOLS)
                .orderByDesc(DeviceMapping::getId));
    }

    @Override
    public R saveOrUpdateMapping(Long userId, DeviceMapping entity) {
        if (entity.getId() != null && !deviceMappingService.exists(userId, entity.getId())) {
            return R.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
        }
        entity.setUserId(userId);
        if (Objects.nonNull(entity.getDomainId()) && !domainMapper.checkDoaminIdDue(userId, entity.getDomainId())) {
            if (entity.getStatus() == 1) {
                return R.fail(SystemErrorType.DOMAIN_IS_DUE);
            }
        }

        DeviceDetail deviceDetail = deviceService.getDetail(entity.getDeviceId());
        if (deviceDetail == null) {
            return R.fail("设备不存在");
        }
        String deviceNo = deviceDetail.getDeviceNo();
        entity.setServerTunnelId(deviceDetail.getServerTunnelId());

        if (!userFlowService.hasFlow(userId)) {
            return R.fail(SystemErrorType.FLOW_IS_DUE);
        }

        entity.setHostname("");
        entity.setUpdateTime(new Date());
        if (Objects.nonNull(entity.getUserDomainId())) {
            UserDomain userDomain = userDomainService.getById(entity.getUserDomainId());
            if (userDomain == null || !userId.equals(userDomain.getUserId())) {
                return R.fail("用户域名 id 不存在");
            }
            entity.setHostname(userDomain.getDomain());
            if (deviceMappingService.checkUserDomain(entity.getId(), entity.getUserDomainId())) {
                return R.fail("用户域名已绑定其他映射");
            }
        }

        if (entity.getDomainId() == null && entity.getStatus() == 1) {
            Domain domain = domainService.getAvailableDomainByUserId(entity.getServerTunnelId(), userId, entity.getPortProtocol());
            if (domain == null) {
                R<Domain> domainR = domainBiz.getAvailableDomainByDeviceMapping(entity);
                if (domainR.isFail()) {
                    log.warn("用户{}套餐问题：{}", userId, domainR.getMsg());
                    return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
                }
                domain = domainR.getData();
            }
            entity.setDomain(domain.getDomain());
            entity.setRemotePort(domain.getType() == 1 ? Integer.parseInt(domain.getDomain()) : null);
            entity.setStatus(1);
            entity.setDomainId(domain.getId());
            entity.setUpdateTime(new Date());
        }

        if (StringUtils.isNotBlank(entity.getDomain()) && existsOtherMappingByDomain(entity)) {
            return R.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
        }

        if (entity.getId() != null) {
            deviceMappingService.updateById(entity);
        } else {
            entity.setCreateTime(new Date());
            deviceMappingService.save(entity);
        }

        DeviceMappingProtocol deviceMappingProtocol = deviceMappingService.getMapping4ProtocolByMappingId(entity.getId());
        if (deviceMappingProtocol == null) {
            return R.success();
        }

        JSONObject data = (JSONObject) JSON.toJSON(deviceMappingProtocol);
        Message msg;
        if (entity.getStatus() == 1) {
            msg = new MsgMapping(data.toJSONString());
        } else {
            log.debug("设备 {} 停用 {} 映射", entity.getDeviceId(), entity.getId());
            msg = new MsgUnMapping(data.toJSONString());
        }
        coonPool.sendMessage(deviceDetail.getServerTunnelId(), deviceNo, msg);

        return R.success();
    }

    private DeviceMapping getOrCreate(Long id) {
        if (id == null) {
            return new DeviceMapping();
        }
        DeviceMapping entity = deviceMappingService.getById(id);
        return entity == null ? new DeviceMapping() : entity;
    }

    private boolean existsOtherMappingByDomain(DeviceMapping entity) {
        return deviceMappingService.count(Wrappers.<DeviceMapping>lambdaQuery()
                .eq(DeviceMapping::getDomain, entity.getDomain())
                .ne(entity.getId() != null, DeviceMapping::getId, entity.getId())) > 0;
    }

    private String toPortProtocol(Integer protocol) {
        if (protocol == null) {
            return null;
        }
        switch (protocol) {
            case DeviceMapping.PROTOCOL_HTTP:
            case DeviceMapping.PROTOCOL_HTTP_HTTPS:
                return "http";
            case DeviceMapping.PROTOCOL_HTTPS:
                return "https";
            case DeviceMapping.PROTOCOL_UDP:
                return "udp";
            case DeviceMapping.PROTOCOL_TCP:
            default:
                return "tcp";
        }
    }
}
