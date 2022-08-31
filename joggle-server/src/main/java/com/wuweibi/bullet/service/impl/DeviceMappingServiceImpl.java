package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.core.builder.MapBuilder;
import com.wuweibi.bullet.device.domain.vo.MappingDeviceVO;
import com.wuweibi.bullet.domain.DeviceMappingDTO;
import com.wuweibi.bullet.domain.dto.DeviceMappingDto;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wuweibi.bullet.core.builder.MapBuilder.newMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Service
public class DeviceMappingServiceImpl extends ServiceImpl<DeviceMappingMapper, DeviceMapping> implements DeviceMappingService {

    @Autowired
    private DeviceService deviceService;

    @Override
    public boolean existsDomain(String domain) {
        return this.baseMapper.existsDomain(newMap(1)
                .setParam("domain", domain).build());
    }

    @Override
    public boolean exists(Long userId, Long id) {

        return this.baseMapper.exists(newMap(2)
                .setParam("userId", userId)
                .setParam("id", id)
                .build());
    }

    @Override
    public DeviceMappingDto getMapping(String host) {
        // TODO host处理


        QueryWrapper params = new QueryWrapper();
        params.eq("domain", host);

        DeviceMapping deviceMapping = this.baseMapper.selectOne(params);
        if(deviceMapping == null){
            return null;
        }
        long deviceId = deviceMapping.getDeviceId();
        Device device = deviceService.getById(deviceId);


        DeviceMappingDto dto = new DeviceMappingDto();
        dto.setDeviceCode(device.getDeviceNo());
        dto.setPort(deviceMapping.getPort());
        dto.setProtocol(deviceMapping.getProtocol());
        dto.setHost(deviceMapping.getHost());

        return dto;
    }

    @Override
    public void deleteByDeviceId(Long deviceId) {
        this.baseMapper.deleteByMap(MapBuilder.newMap(1).setParam(
                "device_id", deviceId
        ).build());
    }

    @Override
    public String getDeviceNo(Long deviceId) {


       return this.baseMapper.selectDeviceNo( deviceId );
    }

    @Override
    public List<DeviceMapping> getAll() {
        return this.baseMapper.selectList(null);
    }

    @Override
    public List<DeviceMapping> getDeviceAll(String deviceNo) {
        return this.baseMapper.selectListByDeviceNo(deviceNo);
    }

    @Override
    public boolean existsDomainId(Long deviceId, Long domainId) {
        return  this.baseMapper.existsDomainId(deviceId, domainId);
    }
    @Override
    public boolean existsDomainId( Long domainId) {
        return this.baseMapper.selectCount(Wrappers.<DeviceMapping>lambdaQuery()
                .eq(DeviceMapping::getDomainId, domainId))>0;
    }

    @Override
    public String getDeviceNoByMappingId(Long mappingId) {
        return this.baseMapper.selectDeviceNoById(mappingId);
    }

    @Override
    public boolean hasOkMapping(Long userId) {
        return this.baseMapper.selectCount(Wrappers.<DeviceMapping>lambdaQuery()
                .eq(DeviceMapping::getUserId, userId)
                .eq(DeviceMapping::getStatus, 1)
        ) > 0;
    }

    @Override
    public List<DeviceMappingDTO> getAllByUserId(Long userId) {
        return this.baseMapper.selectAllByUserId(userId, 1);
    }

    @Override
    public boolean updateDownByUserId(Long userId) {
        return update(Wrappers.<DeviceMapping>lambdaUpdate()
                .eq(DeviceMapping::getUserId, userId)
                .set(DeviceMapping::getStatus, 0)
        );
    }

    @Override
    public int countByDeviceId(Long deviceId) {
        return count(Wrappers.<DeviceMapping>lambdaQuery()
                .eq(DeviceMapping::getDeviceId, deviceId)
        );
    }

    @Override
    public List<MappingDeviceVO> getByDeviceId(Long deviceId) {
        return this.baseMapper.selectByDeviceId(deviceId);
    }

    @Override
    public DeviceMapping getByDomainId(Long deviceId, Long domainId) {
        return this.baseMapper.selectOne(Wrappers.<DeviceMapping>lambdaQuery()
                .eq(DeviceMapping::getDeviceId, deviceId)
                .eq(DeviceMapping::getDomainId, domainId)
        );
    }


}
