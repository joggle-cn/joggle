package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.entity.Device;

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
}
