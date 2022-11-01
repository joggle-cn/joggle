package com.wuweibi.bullet.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.flow.entity.UserFlow;
import org.apache.ibatis.annotations.Param;

/**
 * 用户流量(UserFlow)表数据库访问层
 *
 * @author marker
 * @since 2022-01-09 15:47:16
 */

public interface UserFlowMapper extends BaseMapper<UserFlow> {


    /**
     * 更新流量(增加)
     * @param userId 用户Idd
     * @param flow 流量 kb
     * @return
     */
    boolean updateFlow(@Param("userId") Long userId, @Param("flow") Long flow);

    UserFlow selectUserFlowAndPackageFlow(@Param("userId") Long userId);
}
