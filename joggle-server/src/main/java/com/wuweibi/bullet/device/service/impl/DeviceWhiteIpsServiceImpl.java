package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.DeviceWhiteIpsParam;
import com.wuweibi.bullet.device.domain.DeviceWhiteIpsVO;
import com.wuweibi.bullet.device.mapper.DeviceWhiteIpsMapper;
import com.wuweibi.bullet.device.entity.DeviceWhiteIps;
import com.wuweibi.bullet.device.service.DeviceWhiteIpsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import java.util.stream.Collectors;

/**
 * (DeviceWhiteIps)表服务实现类
 *
 * @author marker
 * @since 2022-10-11 22:19:15
 */
@Service
public class DeviceWhiteIpsServiceImpl extends ServiceImpl<DeviceWhiteIpsMapper, DeviceWhiteIps> implements DeviceWhiteIpsService {


    @Override
    public Page<DeviceWhiteIpsVO> getPage(Page pageInfo, DeviceWhiteIpsParam params) {
        LambdaQueryWrapper<DeviceWhiteIps> qw = Wrappers.lambdaQuery();
        Page<DeviceWhiteIps> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<DeviceWhiteIpsVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            DeviceWhiteIpsVO vo = new DeviceWhiteIpsVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public DeviceWhiteIps getByDeviceId(Long deviceId) {
        return this.baseMapper.selectOne(Wrappers.<DeviceWhiteIps>lambdaQuery()
                .eq(DeviceWhiteIps::getDeviceId, deviceId));
    }


}
