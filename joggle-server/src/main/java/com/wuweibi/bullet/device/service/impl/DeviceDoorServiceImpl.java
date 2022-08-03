package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.DeviceDoorParam;
import com.wuweibi.bullet.device.domain.DeviceDoorVO;
import com.wuweibi.bullet.device.entity.DeviceDoor;
import com.wuweibi.bullet.device.mapper.DeviceDoorMapper;
import com.wuweibi.bullet.device.service.DeviceDoorService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 设备任意门(DeviceDoor)表服务实现类
 *
 * @author marker
 * @since 2022-08-03 21:21:27
 */
@Service
public class DeviceDoorServiceImpl extends ServiceImpl<DeviceDoorMapper, DeviceDoor> implements DeviceDoorService {


    @Override
    public Page<DeviceDoorVO> getPage(Page pageInfo, DeviceDoorParam params) {

        LambdaQueryWrapper<DeviceDoor> qw = Wrappers.lambdaQuery();
        Page<DeviceDoor> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<DeviceDoorVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            DeviceDoorVO vo = new DeviceDoorVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public DeviceDoor getByDeviceId(Long deviceId) {
        return this.baseMapper.selectOne(Wrappers.<DeviceDoor>lambdaQuery()
                .eq(DeviceDoor::getDeviceId, deviceId));
    }
}
