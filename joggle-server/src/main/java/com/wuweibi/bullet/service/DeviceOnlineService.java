package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.device.domain.dto.DeviceOnlineInfoDTO;
import com.wuweibi.bullet.entity.DeviceOnline;

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
    void saveOrUpdateOnline(String deviceNo, String ip, String mac, String clientVersion);


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

    /**
     * 保存或者更新在线设备信息
     * @param deviceOnlineInfoDTO 在线设备信息
     * @return
     */
    boolean saveOrUpdate(DeviceOnlineInfoDTO deviceOnlineInfoDTO);

    /**
     * 获取在线设备信息
     * @param deviceId 设备id
     * @return
     */
    DeviceOnline getByDeviceNo(String deviceId);

}
