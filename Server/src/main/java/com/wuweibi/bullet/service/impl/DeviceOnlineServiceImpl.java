package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.mapper.DeviceOnlineMapper;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Service
public class DeviceOnlineServiceImpl extends ServiceImpl<DeviceOnlineMapper, DeviceOnline> implements DeviceOnlineService {

    @Override
    public void saveOrUpdateOnline(String deviceId) {

        EntityWrapper ew = new EntityWrapper(new DeviceOnline());
        ew.where("deviceId = {0}", deviceId);

        int count = this.baseMapper.selectCount(ew);

        DeviceOnline deviceOnline = new DeviceOnline();
        deviceOnline.setDeviceId(deviceId);
        deviceOnline.setStatus(1);// 等待被绑定
        deviceOnline.setUpdateTime(new Date());

        if(count > 0){
            this.baseMapper.update(deviceOnline, ew);
        }else{// save
            this.baseMapper.insert(deviceOnline);
        }
    }

    @Override
    public void updateOutLine(String deviceId) {
        EntityWrapper ew = new EntityWrapper(new DeviceOnline());
        ew.where("deviceId = {0}", deviceId);

        DeviceOnline deviceOnline = new DeviceOnline();
        deviceOnline.setDeviceId(deviceId);
        deviceOnline.setStatus(-1);// 下线后的设备部可绑定
        deviceOnline.setUpdateTime(new Date());
        this.baseMapper.update(deviceOnline, ew);
    }
}
