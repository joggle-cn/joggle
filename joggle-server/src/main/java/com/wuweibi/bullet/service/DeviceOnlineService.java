package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.device.domain.dto.DeviceOnlineInfoDTO;
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

    /**
     * 检查设备状态
     * @return
     */
    boolean checkDeviceStatus();


    boolean updateDeviceStatus(String deviceNo, int status);


    /**
     * 批量更新设备状态
     * @param deviceNoList 设备清单
     * @param status 设备状态
     * @return
     */
    int batchUpdateStatus(List<String> deviceNoList, int status);

    /**
     * 下线通道的所有设备
     * @param tunnelId 通道id
     * @return
     */
    int updateOutLineByTunnelId(Integer tunnelId);
}
