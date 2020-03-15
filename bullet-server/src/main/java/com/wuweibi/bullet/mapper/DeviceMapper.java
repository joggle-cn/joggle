package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.entity.Device;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceMapper extends BaseMapper<Device> {


    /**
     * 更新
     * @param params 参数
     */
    void updateName(Map<String, Object> params);


    /**
     * 判断设备是否存在
     * @param build
     * @return
     */
    boolean exists(Map<String, Object> build);


    /**
     * 判断设备是否存在
     * @param deviceId
     * @return
     */
    boolean existsDevice(Map<String, Object> deviceId);
}