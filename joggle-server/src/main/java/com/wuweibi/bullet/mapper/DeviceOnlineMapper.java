package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.DeviceOnline;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceOnlineMapper extends BaseMapper<DeviceOnline> {

    /**
     * 更新所有设备状态为下线
     */
    void updateStatusDown();

    /**
     * 更新状态
     * @param deviceOnline
     */
    void updateStatus(String deviceOnline);

    List<DeviceOnline> selectDiscoveryDevice(String ip);


    /**
     * 更新设备在线状态
     * @param deviceNo 设备编码
     * @param status 状态
     * @return
     */
    boolean updateDeviceStatus(@Param("deviceNo") String deviceNo,@Param("status") int status);

    /**
     * 批量更新设备状态
     * @param deviceNoList 设备清单
     * @param status 设备状态
     * @return
     */
    int batchUpdateStatus(@Param("list") List<String> deviceNoList,@Param("status") int status);

    /**
     * 根据通道id批量更新设备状态为下线
     *
     * @param tunnelId 通道id
     * @return
     */
    int updateOutLineByTunnelId(@Param("tunnelId") Integer tunnelId);

}
