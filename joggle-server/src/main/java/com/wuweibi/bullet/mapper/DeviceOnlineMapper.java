package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.DeviceOnline;

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
}
