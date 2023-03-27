package com.wuweibi.bullet.service;

import com.wuweibi.bullet.dashboard.domain.DeviceCountInfoVO;
import com.wuweibi.bullet.dashboard.domain.DeviceDateItemVO;
import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.vo.CountVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
public interface CountService {

    /**
     * 获取 全站统计数据
     * @return
     */
    CountVO getCountInfo();

    /**
     * 用户统计汇总数据
     * @param userId 用户ID
     * @return
     */
    UserCountVO getUserCountInfo(Long userId);

    /**
     * 统计设备流量排行
     * @param userId 用户ID
     * @param type
     * @return
     */
    List<DeviceCountInfoVO> getUserDeviceRank(Long userId, Integer type);

    /**
     * 统计设备流量趋势
     * @param userId 用户id
     * @param deviceId 设备id
     * @return
     */
    List<DeviceDateItemVO> getUserDeviceTrend(Long userId, Long deviceId);

    List<DeviceDateItemVO> getAllFlowTrend(int day);
}
