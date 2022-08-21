package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.DeviceProxyParam;
import com.wuweibi.bullet.device.domain.DeviceProxyVO;
import com.wuweibi.bullet.device.entity.DeviceProxy;
import com.wuweibi.bullet.device.mapper.DeviceProxyMapper;
import com.wuweibi.bullet.device.service.DeviceProxyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 设备代理(DeviceProxy)表服务实现类
 *
 * @author marker
 * @since 2022-08-19 21:00:28
 */
@Service
public class DeviceProxyServiceImpl extends ServiceImpl<DeviceProxyMapper, DeviceProxy> implements DeviceProxyService {


    @Override
    public Page<DeviceProxyVO> getPage(Page pageInfo, DeviceProxyParam params) {

        LambdaQueryWrapper<DeviceProxy> qw = Wrappers.lambdaQuery();
        Page<DeviceProxy> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<DeviceProxyVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            DeviceProxyVO vo = new DeviceProxyVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public DeviceProxy getByDeviceId(Long deviceId) {
        return this.baseMapper.selectOne(Wrappers.<DeviceProxy>lambdaQuery()
                .eq(DeviceProxy::getDeviceId, deviceId));
    }
}
