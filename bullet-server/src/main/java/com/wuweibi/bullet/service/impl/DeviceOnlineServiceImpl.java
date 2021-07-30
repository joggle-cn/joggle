package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.dto.DeviceOnlineInfoDTO;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.mapper.DeviceOnlineMapper;
import com.wuweibi.bullet.service.DeviceOnlineService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Service
public class DeviceOnlineServiceImpl extends ServiceImpl<DeviceOnlineMapper, DeviceOnline> implements DeviceOnlineService {

    @Override
    public void saveOrUpdateOnline(String deviceNo, String ip, String mac) {

        QueryWrapper ew = new QueryWrapper();
        ew.eq("deviceNo", deviceNo);

        int count = this.baseMapper.selectCount(ew);

        DeviceOnline deviceOnline = new DeviceOnline();
        deviceOnline.setDeviceNo(deviceNo);
        deviceOnline.setStatus(1);// 等待被绑定（在线)
        deviceOnline.setUpdateTime(new Date());
        deviceOnline.setIntranetIp(ip); // ip
        deviceOnline.setMacAddr(mac); // mac

        if (count > 0) {
            this.baseMapper.update(deviceOnline, ew);
        } else {// save
            this.baseMapper.insert(deviceOnline);
        }
    }

    @Override
    public void updateOutLine(String deviceId) {
        QueryWrapper ew = new QueryWrapper();
        ew.eq("deviceNo", deviceId);

        DeviceOnline deviceOnline = new DeviceOnline();
        deviceOnline.setDeviceNo(deviceId);
        deviceOnline.setStatus(-1);// 下线后的设备部可绑定
        deviceOnline.setUpdateTime(new Date());
        this.baseMapper.update(deviceOnline, ew);
    }

    @Override
    public DeviceOnline selectByDeviceNo(String deviceNo) {
        QueryWrapper ew = new QueryWrapper();
        ew.eq("deviceNo", deviceNo);
        return this.baseMapper.selectOne(ew);
    }

    @Override
    public boolean existsOnline(String deviceNo) {

        QueryWrapper ew = new QueryWrapper();
        ew.eq("deviceNo", deviceNo);
        ew.eq("status", 1);

        int count = this.baseMapper.selectCount(ew);
        return count > 0;
    }

    @Override
    public void allDownNow() {
        this.baseMapper.updateStatusDown();
    }

    @Override
    public boolean saveOrUpdate(DeviceOnlineInfoDTO deviceInfo) {
        String deviceNo = deviceInfo.getDeviceNo();
        DeviceOnline deviceOnline = this.baseMapper.selectOne(Wrappers.<DeviceOnline>lambdaQuery()
                .eq(DeviceOnline::getDeviceNo, deviceNo));
        if(deviceOnline == null){
            deviceOnline = new DeviceOnline();
            deviceOnline.setDeviceNo(deviceNo);
            deviceOnline.setStatus(1);// 等待被绑定（在线)
        }
        deviceOnline.setPublicIp(deviceInfo.getPublicIp());
        deviceOnline.setUpdateTime(new Date());

        if (deviceOnline.getId() != null) {
            this.baseMapper.updateById(deviceOnline);
        } else {// save
            this.baseMapper.insert(deviceOnline);
        }
        return true;
    }
}
