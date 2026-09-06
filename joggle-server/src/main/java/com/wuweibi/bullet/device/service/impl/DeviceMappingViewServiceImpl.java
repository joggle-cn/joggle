package com.wuweibi.bullet.device.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wuweibi.bullet.config.cache.RedisTemplateConfig;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingClientVO;
import com.wuweibi.bullet.device.domain.vo.MappingDeviceVO;
import com.wuweibi.bullet.device.service.DeviceMappingViewService;
import com.wuweibi.bullet.enums.ProtocolTypeEnum;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.wuweibi.bullet.alias.CacheCode.DEVICE_MAPPING_STATISTICS_FLOW_TODAY;
import static com.wuweibi.bullet.alias.CacheCode.DEVICE_MAPPING_STATISTICS_LINK_TODAY;

@Service
public class DeviceMappingViewServiceImpl implements DeviceMappingViewService {

    private static final Integer[] PORT_PROTOCOLS = new Integer[]{2, 5};
    private static final Integer[] DOMAIN_PROTOCOLS = new Integer[]{1, 3, 4};

    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource(name = RedisTemplateConfig.BEAN_REDIS_TEMPLATE)
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MappingGroup getMappingGroup(Long deviceId, DeviceDetailVO deviceInfo) {
        List<MappingDeviceVO> mappingList = deviceMappingService.getByDeviceId(deviceId);

        String date = DateUtil.format(new Date(), "yyyyMMdd");
        String keyBytes = String.format(DEVICE_MAPPING_STATISTICS_FLOW_TODAY, date);
        String keyLink = String.format(DEVICE_MAPPING_STATISTICS_LINK_TODAY, date);
        BoundHashOperations<String, Object, Object> keyBytesMap = redisTemplate.boundHashOps(keyBytes);
        BoundHashOperations<String, Object, Object> keyLinkMap = redisTemplate.boundHashOps(keyLink);

        MappingGroup group = new MappingGroup();
        group.setPortList(mappingList.stream()
                .filter(item -> ArrayUtils.contains(PORT_PROTOCOLS, item.getProtocol()))
                .map(item -> toClientPortMapping(item, deviceInfo, keyBytesMap, keyLinkMap))
                .collect(Collectors.toList()));
        group.setDomainList(mappingList.stream()
                .filter(item -> ArrayUtils.contains(DOMAIN_PROTOCOLS, item.getProtocol()))
                .map(item -> toClientDomainMapping(item, deviceInfo, keyBytesMap, keyLinkMap))
                .collect(Collectors.toList()));
        return group;
    }

    @Override
    public List<DeviceMappingClientVO> getPortMappings(Long deviceId, DeviceDetailVO deviceInfo) {
        return getMappingGroup(deviceId, deviceInfo).getPortList();
    }

    @Override
    public List<DeviceMappingClientVO> getDomainMappings(Long deviceId, DeviceDetailVO deviceInfo) {
        return getMappingGroup(deviceId, deviceInfo).getDomainList();
    }

    @Override
    public DeviceMappingClientVO getClientMapping(com.wuweibi.bullet.entity.DeviceMapping mapping, DeviceDetailVO deviceInfo) {
        DeviceMappingClientVO vo = new DeviceMappingClientVO();
        BeanUtils.copyProperties(mapping, vo);
        if (vo.getProtocol() != null && vo.getProtocol() != 2 && vo.getProtocol() != 5) {
            String protocol = ProtocolTypeEnum.getProtocol(vo.getProtocol());
            vo.setDomain(vo.getDomain() + "." + deviceInfo.getServerAddr());
            String domain = vo.getDomain();
            if (StringUtil.isNotBlank(vo.getHostname())) {
                domain = vo.getHostname();
            }
            vo.setUrl(String.format("%s://%s", protocol, domain));
        } else {
            String protocol = ProtocolTypeEnum.getProtocol(vo.getProtocol());
            vo.setDomain(deviceInfo.getServerAddr() + ":" + vo.getRemotePort());
            String domain = vo.getDomain();
            if (StringUtil.isNotBlank(vo.getHostname())) {
                domain = vo.getHostname();
            }
            vo.setUrl(String.format("%s://%s", protocol, domain));
        }

        fillStatistics(vo,
                redisTemplate.boundHashOps(String.format(DEVICE_MAPPING_STATISTICS_FLOW_TODAY, DateUtil.format(new Date(), "yyyyMMdd"))),
                redisTemplate.boundHashOps(String.format(DEVICE_MAPPING_STATISTICS_LINK_TODAY, DateUtil.format(new Date(), "yyyyMMdd"))));
        return vo;
    }

    private DeviceMappingClientVO toClientPortMapping(MappingDeviceVO item,
                                                      DeviceDetailVO deviceInfo,
                                                      BoundHashOperations<String, Object, Object> keyBytesMap,
                                                      BoundHashOperations<String, Object, Object> keyLinkMap) {
        DeviceMappingClientVO vo = toClientVO(item);
        String protocol = ProtocolTypeEnum.getProtocol(vo.getProtocol());
        vo.setDomain(deviceInfo.getServerAddr() + ":" + vo.getRemotePort());
        String domain = vo.getDomain();
        if (StringUtil.isNotBlank(vo.getHostname())) {
            domain = vo.getHostname();
        }
        vo.setUrl(String.format("%s://%s", protocol, domain));
        fillStatistics(vo, keyBytesMap, keyLinkMap);
        return vo;
    }

    private DeviceMappingClientVO toClientDomainMapping(MappingDeviceVO item,
                                                        DeviceDetailVO deviceInfo,
                                                        BoundHashOperations<String, Object, Object> keyBytesMap,
                                                        BoundHashOperations<String, Object, Object> keyLinkMap) {
        DeviceMappingClientVO vo = toClientVO(item);
        String protocol = ProtocolTypeEnum.getProtocol(vo.getProtocol());
        if (StringUtil.isBlank(vo.getDomain())) {
            String domain = vo.getHost() + ":" + vo.getPort();
            vo.setUrl(String.format("%s://%s", protocol, domain));
        } else {
            vo.setDomain(vo.getDomain() + "." + deviceInfo.getServerAddr());
            String domain = vo.getDomain();
            if (StringUtil.isNotBlank(vo.getHostname())) {
                domain = vo.getHostname();
            }
            vo.setUrl(String.format("%s://%s", protocol, domain));
        }
        fillStatistics(vo, keyBytesMap, keyLinkMap);
        return vo;
    }

    private DeviceMappingClientVO toClientVO(MappingDeviceVO item) {
        DeviceMappingClientVO vo = new DeviceMappingClientVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }

    private void fillStatistics(DeviceMappingClientVO item,
                                BoundHashOperations<String, Object, Object> keyBytesMap,
                                BoundHashOperations<String, Object, Object> keyLinkMap) {
        Integer flowKb = (Integer) keyBytesMap.get(item.getId().toString());
        Integer linkNum = (Integer) keyLinkMap.get(item.getId().toString());
        item.setTodayFlow(new BigDecimal(flowKb == null ? 0 : flowKb).divide(BigDecimal.valueOf(1024)));
        item.setLink(linkNum == null ? 0 : linkNum);
    }
}
