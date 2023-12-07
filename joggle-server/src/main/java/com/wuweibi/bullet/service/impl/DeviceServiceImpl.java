package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceAdminParam;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceListVO;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.domain.dto.DeviceDto;
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
    private WebsocketPool coonPool;


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
    public boolean existsNoBindDevice(String deviceId) {
        return this.baseMapper.existsNoBindDevice(newMap(1)
                .setParam("deviceId",deviceId)
                .build());
    }

    @Override
    public void wakeUp(Long userId, String mac) {
        // 查询所有设备是因为大部分用户的设备数量并不多。
        List<Device> deviceList = getListByUserId(userId);
        MsgWOL msg = new MsgWOL(mac);
        deviceList.forEach(item -> {
            String deviceNo = item.getDeviceNo();
            coonPool.boradcast(item.getServerTunnelId(), deviceNo, msg);
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

    @Override
    public DeviceDetailVO getDeviceInfoById(Long deviceId) {
        return this.baseMapper.selectDeviceInfoById(deviceId);
    }

    @Override
    public Long getCountByUserId(Long userId) {
        return this.baseMapper.selectCount(Wrappers.<Device>lambdaQuery()
                .eq(Device::getUserId, userId));
    }

    @Override
    public List<DeviceOption> getOptionListByUserId(Long userId) {
        return this.baseMapper.selectOptionListByUserId(userId);
    }

    @Override
    public boolean existsDeviceId(Long deviceId) {
        return this.baseMapper.selectCount(Wrappers.<Device>lambdaQuery().eq(Device::getId, deviceId)) > 0;
    }

    @Override
    public boolean existsUserDeviceId(Long userId, Long deviceId) {
        return this.baseMapper.selectCount(Wrappers.<Device>lambdaQuery()
                .eq(Device::getUserId, userId)
                .eq(Device::getId, deviceId)) > 0;
    }

    @Override
    public boolean removeUserId(Long deviceId) {
        return this.update(Wrappers.<Device>lambdaUpdate()
                .eq(Device::getId, deviceId)
                .set(Device::getUserId, null)
        );
    }

    @Override
    public List<DeviceDto> getWebListByUserId(Long userId) {
        return this.baseMapper.selectWebListByUserId(userId);
    }

    @Override
    public DeviceDetail getDetail(Long deviceId) {
        return this.baseMapper.selectDetail(deviceId);
    }

    @Override
    public DeviceDetail getDetailByDeviceNo(String deviceNo) {
        return this.baseMapper.selectDetailByDeviceNo(deviceNo);
    }

    @Override
    public Page<DeviceListVO> getAdminList(Page pageInfo, DeviceAdminParam params) {
        return  this.baseMapper.selectAdminList(pageInfo, params);
    }


    private List<Device> getListByUserId(Long userId){
       return this.baseMapper.selectList(Wrappers.<Device>lambdaQuery().eq(Device::getUserId, userId));
    }
}
