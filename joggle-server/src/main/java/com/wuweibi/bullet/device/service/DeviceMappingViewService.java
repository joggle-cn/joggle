package com.wuweibi.bullet.device.service;

import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingClientVO;
import com.wuweibi.bullet.entity.DeviceMapping;
import lombok.Data;

import java.util.List;

public interface DeviceMappingViewService {

    MappingGroup getMappingGroup(Long deviceId, DeviceDetailVO deviceInfo);

    List<DeviceMappingClientVO> getPortMappings(Long deviceId, DeviceDetailVO deviceInfo);

    List<DeviceMappingClientVO> getDomainMappings(Long deviceId, DeviceDetailVO deviceInfo);

    DeviceMappingClientVO getClientMapping(DeviceMapping mapping, DeviceDetailVO deviceInfo);

    @Data
    class MappingGroup {
        private List<DeviceMappingClientVO> portList;
        private List<DeviceMappingClientVO> domainList;
    }
}
