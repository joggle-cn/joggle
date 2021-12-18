package com.wuweibi.bullet.service;

import com.wuweibi.bullet.dashboard.domain.DeviceCountInfoVO;
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
}
