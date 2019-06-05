package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.entity.DeviceOnline;
import com.baomidou.mybatisplus.mapper.BaseMapper;

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

}