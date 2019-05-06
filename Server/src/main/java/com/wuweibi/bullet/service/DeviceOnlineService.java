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
     */
    void saveOrUpdateOnline(String deviceNo, String ip);


    /**
     * 设备下线
     * @param deviceId
     */
    void updateOutLine(String deviceId);
}
