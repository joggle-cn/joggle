package com.wuweibi.bullet.device.service;

import com.wuweibi.bullet.device.domain.dto.DeviceMappingDomainDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingPortDTO;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;

import java.util.List;

public interface DeviceMappingManagerService {

    R saveOrUpdateMapping(Long userId, DeviceMapping entity);

    R saveDomainMapping(Long userId, DeviceMappingDomainDTO dto);

    R savePortMapping(Long userId, DeviceMappingPortDTO dto);

    List<DeviceMapping> listDomainMappings(Long userId, Long deviceId);

    List<DeviceMapping> listPortMappings(Long userId, Long deviceId);
}
