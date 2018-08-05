package com.wuweibi.bullet.service.impl;

import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.mapper.DeviceMapper;
import com.wuweibi.bullet.service.DeviceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import static com.wuweibi.bullet.builder.MapBuilder.newMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Override
    public void updateName(Long id, String name) {

        this.baseMapper.updateName(newMap(2)
                .setParam("id",id)
                .setParam("name", name)
                .build());
    }

    @Override
    public boolean exists(Long userId, Long id) {
        return this.baseMapper.exists(newMap(2)
                .setParam("userId",userId)
                .setParam("id", id)
                .build());
    }

    @Override
    public boolean existsDevice(String deviceId) {
        return this.baseMapper.existsDevice(newMap(1)
                .setParam("deviceId",deviceId)
                .build());
    }
}
