package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogParam;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogVO;
import com.wuweibi.bullet.device.entity.DeviceOnlineLog;
import com.wuweibi.bullet.device.mapper.DeviceOnlineLogMapper;
import com.wuweibi.bullet.device.service.DeviceOnlineLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 设备在线日志(DeviceOnlineLog)表服务实现类
 *
 * @author marker
 * @since 2023-01-23 19:46:35
 */
@Service
public class DeviceOnlineLogServiceImpl extends ServiceImpl<DeviceOnlineLogMapper, DeviceOnlineLog> implements DeviceOnlineLogService {


    @Override
    public Page<DeviceOnlineLogVO> getPage(Page pageInfo, DeviceOnlineLogParam params) {

        LambdaQueryWrapper<DeviceOnlineLog> qw = Wrappers.lambdaQuery();
        Page<DeviceOnlineLog> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<DeviceOnlineLogVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            DeviceOnlineLogVO vo = new DeviceOnlineLogVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }
}
