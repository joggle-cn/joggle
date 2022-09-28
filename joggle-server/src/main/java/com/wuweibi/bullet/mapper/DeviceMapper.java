package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
    boolean existsNoBindDevice(Map<String, Object> deviceId);


    /**
     * 获取设备详情
     * @param deviceId 设备id
     * @return
     */
    DeviceDetailVO selectDeviceInfoById(@Param("deviceId") Long deviceId);

    /**
     * 获取设备下拉列表
     * @param userId 用户id
     * @return
     */
    List<DeviceOption> selectOptionListByUserId(@Param("userId") Long userId);

    List<DeviceDto> selectWebListByUserId(@Param("userId")Long userId);

    /**
     * 获取设备详情
     * @param deviceId 设备id
     * @return
     */
    DeviceDetail selectDetail(@Param("deviceId") Long deviceId);

    DeviceDetail selectDetailByDeviceNo(@Param("deviceNo") String deviceNo);

}