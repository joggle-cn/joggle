package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.alias.CacheBlock;
import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceAdminParam;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceListVO;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.mapper.DeviceMapper;
import com.wuweibi.bullet.mapper.DeviceOnlineMapper;
import com.wuweibi.bullet.protocol.MsgWOL;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.SpringUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
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

    @Resource
    private RedisCacheManager redisCacheManager;


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
        Device device = this.baseMapper.selectById(deviceId);
        if (device == null) {
            return false;
        }
        this.update(Wrappers.<Device>lambdaUpdate()
                .eq(Device::getId, deviceId)
                .set(Device::getUserId, null)
        );

        // 清理设备缓存
        Cache cache = redisCacheManager.getCache(CacheBlock.CACHE_DEVICE_DETAIL);
        cache.evict(device.getDeviceNo());
        return true;
    }

    @Override
    public Boolean removeUserIdByDeviceNo(@NotNull String deviceNo) {
        this.update(Wrappers.<Device>lambdaUpdate()
                .eq(Device::getDeviceNo, deviceNo)
                .set(Device::getUserId, null)
        );

        // 清理设备缓存
        Cache cache = redisCacheManager.getCache(CacheBlock.CACHE_DEVICE_DETAIL);
        cache.evict(deviceNo);
        return true;
    }

    @Override
    public List<DeviceDto> getWebListByUserId(Long userId) {
        return this.baseMapper.selectWebListByUserId(userId);
    }

    @Override
    public DeviceDetail getDetail(Long deviceId) {
        return this.baseMapper.selectDetail(deviceId);
    }


    @Cacheable(cacheNames = CacheBlock.CACHE_DEVICE_DETAIL, key = "#deviceNo")
    @Override
    public DeviceDetail getDetailByDeviceNo(String deviceNo) {
        return this.baseMapper.selectDetailByDeviceNo(deviceNo);
    }

    @Override
    public Page<DeviceListVO> getAdminList(Page pageInfo, DeviceAdminParam params) {
        return  this.baseMapper.selectAdminList(pageInfo, params);
    }

    @Override
    public Device bindDevice(Long userId, String deviceNo) {
        DeviceService deviceService = SpringUtils.getBean(DeviceService.class);
        // 获取设备信息
        Device device = deviceService.getByDeviceNo(deviceNo);
        if (device!= null && device.getUserId() != null) {
            throw new RException(SystemErrorType.DEVICE_OTHER_BIND);
        }

        if (device == null) {
            // 给当前用户存储最新的设备数据
            device = new Device();
            device.setDeviceNo(deviceNo);
            device.setUserId(userId);
            device.setCreateTime(new Date());
            device.setName(deviceNo);
        }

        // 生成设备秘钥
        String deviceSecret = Md5Crypt.md5Crypt(deviceNo.getBytes(), null, "");
        device.setDeviceSecret(deviceSecret);
        device.setUserId(userId);
        deviceService.saveOrUpdate(device);

        // 清理设备缓存
        Cache cache = redisCacheManager.getCache(CacheBlock.CACHE_DEVICE_DETAIL);
        cache.evict(deviceNo);
        return device;
    }


    private List<Device> getListByUserId(Long userId){
       return this.baseMapper.selectList(Wrappers.<Device>lambdaQuery().eq(Device::getUserId, userId));
    }
}
