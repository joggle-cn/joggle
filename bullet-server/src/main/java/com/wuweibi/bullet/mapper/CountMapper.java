package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.dashboard.domain.DeviceCountInfoVO;
import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.vo.CountVO;
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
public interface CountMapper {


    /**
     * 统计首页情况
     * @return
     */
    CountVO selectCountInfo();

    /**
     * 统计用户的流量情况
     * @param userId 用户ID
     * @return
     */
    UserCountVO selectUserCountInfo(@Param("userId") Long userId);

    /**
     * 统计设备流量排行
     * @param userId 用户ID
     * @param type
     * @return
     */
    List<DeviceCountInfoVO> selectUserDeviceRank(@Param("userId") Long userId, @Param("type") Integer type);
}
