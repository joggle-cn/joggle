package com.wuweibi.bullet.service.impl;

import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.service.DeviceMappingService;
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
public class DeviceMappingServiceImpl extends ServiceImpl<DeviceMappingMapper, DeviceMapping> implements DeviceMappingService {

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
}
