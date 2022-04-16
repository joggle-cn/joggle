package com.wuweibi.bullet.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.Device;
import io.lettuce.core.dynamic.annotation.Param;

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


    /**
     * 获取设备详情
     * @param deviceId 设备id
     * @return
     */
    JSONObject selectDeviceInfoById(@Param("deviceId") Long deviceId);
}