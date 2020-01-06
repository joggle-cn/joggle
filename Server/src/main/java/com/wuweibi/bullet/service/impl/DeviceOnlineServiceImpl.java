package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.mapper.DeviceOnlineMapper;
import com.wuweibi.bullet.service.DeviceOnlineService;
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
    public void saveOrUpdateOnline(String deviceNo, String ip, String mac) {

        EntityWrapper ew = new EntityWrapper(new DeviceOnline());
        ew.where("deviceNo = {0}", deviceNo);

        int count = this.baseMapper.selectCount(ew);

        DeviceOnline deviceOnline = new DeviceOnline();
        deviceOnline.setDeviceNo(deviceNo);
        deviceOnline.setStatus(1);// 等待被绑定（在线)
        deviceOnline.setUpdateTime(new Date());
        deviceOnline.setIntranetIp(ip); // ip
        deviceOnline.setMacAddr(mac); // mac

        if(count > 0){
            this.baseMapper.update(deviceOnline, ew);
        }else{// save
            this.baseMapper.insert(deviceOnline);
        }
    }

    @Override
    public void updateOutLine(String deviceId) {
        EntityWrapper ew = new EntityWrapper(new DeviceOnline());
        ew.where("deviceNo = {0}", deviceId);

        DeviceOnline deviceOnline = new DeviceOnline();
        deviceOnline.setDeviceNo(deviceId);
        deviceOnline.setStatus(-1);// 下线后的设备部可绑定
        deviceOnline.setUpdateTime(new Date());
        this.baseMapper.update(deviceOnline, ew);
    }

    @Override
    public DeviceOnline selectByDeviceNo(String deviceNo) {
        EntityWrapper entityWrapper = new EntityWrapper<DeviceOnline>();
        entityWrapper.setEntity(new DeviceOnline(deviceNo));
        return this.selectOne(entityWrapper);
    }

    @Override
    public boolean existsOnline(String deviceNo) {
        EntityWrapper ew = new EntityWrapper(new DeviceOnline());
        ew.where("deviceNo = {0}", deviceNo);
        ew.where("status = {0}", 1);
        int count =  this.selectCount(ew);
        return count > 0;
    }

    @Override
    public void allDownNow() {
        this.baseMapper.updateStatusDown();
    }

    @Override
    public void saveOrUpdateOnlineStatus(String deviceNo) {
        EntityWrapper ew = new EntityWrapper(new DeviceOnline());
        ew.where("deviceNo = {0}", deviceNo);
        int count = this.baseMapper.selectCount(ew);
        DeviceOnline deviceOnline = new DeviceOnline();
        deviceOnline.setDeviceNo(deviceNo);
        deviceOnline.setStatus(1);// 等待被绑定（在线)
        deviceOnline.setUpdateTime(new Date());

        if(count > 0){
            this.baseMapper.updateStatus(deviceNo);
        }else{// save
            this.baseMapper.insert(deviceOnline);
        }
    }
}
