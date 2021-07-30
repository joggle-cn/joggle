package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.mapper.DeviceMapper;
import com.wuweibi.bullet.mapper.DeviceOnlineMapper;
import com.wuweibi.bullet.protocol.MsgWOL;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {


    @Resource
    private CoonPool coonPool;


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

    @Override
    public void wakeUp(Long userId, String mac) {
        // 查询所有设备是因为大部分用户的设备数量并不多。
        List<Device> deviceList = getListByUserId(userId);
        MsgWOL msg = new MsgWOL(mac);
        deviceList.forEach(item ->{
            String deviceNo = item.getDeviceNo();
            coonPool.boradcast(deviceNo, msg);
        });
    }

    @Override
    public Device getByDeviceNo(String deviceNo) {
        return this.baseMapper.selectOne(Wrappers.<Device>lambdaQuery().eq(Device::getDeviceNo, deviceNo));
    }

    @Resource
    private DeviceOnlineMapper deviceOnlineMapper;

    @Override
    public List<DeviceOnline> getDiscoveryDevice(String ip) {
        return this.deviceOnlineMapper.selectDiscoveryDevice(ip);
    }


    private List<Device> getListByUserId(Long userId){
       return this.baseMapper.selectList(Wrappers.<Device>lambdaQuery().eq(Device::getUserId, userId));
    }
}
