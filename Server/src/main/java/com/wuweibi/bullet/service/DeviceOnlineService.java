package com.wuweibi.bullet.service;

import com.wuweibi.bullet.entity.DeviceOnline;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceOnlineService extends IService<DeviceOnline> {


    /**
     * 保存或更新为在线状态
     * @param deviceNo
     * @param ip
     * @param mac
     */
    void saveOrUpdateOnline(String deviceNo, String ip, String mac);


    /**
     * 设备下线
     * @param deviceId
     */
    void updateOutLine(String deviceId);

    DeviceOnline selectByDeviceNo(String deviceNo);

    /**
     * 是否存在设备
     * @param deviceNo
     * @return
     */
    boolean existsOnline(String deviceNo);

    /**
     * 所有设备下线
     */
    void allDownNow();

    void saveOrUpdateOnlineStatus(String deviceNo);
}
