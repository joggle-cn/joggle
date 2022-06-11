package com.wuweibi.bullet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceOnline;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceService extends IService<Device> {


    /**
     * 更新设备名称
     * @param id Id
     * @param name 名称
     */
    void updateName(Long id, String name);

    /**
     * 判断用户是否存在设备
     * @param userId 用户Id
     * @param deviceId 设备ID
     * @return
     */
    boolean exists(Long userId, Long deviceId);


    /**
     * 判断设备是否存在
     * @param deviceId
     * @return
     */
    boolean existsDevice(String deviceId);

    void wakeUp(Long userId, String mac);

    /**
     * 根据设备编号获取设备信息
     * @param deviceNo 设备编号
     * @return
     */
    Device getByDeviceNo(String deviceNo);

    /**
     * 设备发现（同样出口IP的设备)
     * @param ip
     * @return
     */
    List<DeviceOnline> getDiscoveryDevice(String ip);


    /**
     * 获取设备详情
     * @param deviceId 设备id
     * @return
     */
    JSONObject getDeviceInfoById(Long deviceId);

    /**
     * 获取用户绑定的设备数量
     * @param userId 用户id
     * @return 绑定设备的数量
     */
    int getCountByUserId(Long userId);

}
