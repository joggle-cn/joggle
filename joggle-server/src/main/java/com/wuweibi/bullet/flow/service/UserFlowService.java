package com.wuweibi.bullet.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.flow.entity.UserFlow;

/**
 * 用户流量(UserFlow)表服务接口
 *
 * @author marker
 * @since 2022-01-09 15:47:07
 */
public interface UserFlowService extends IService<UserFlow> {


    /**
     * 获取用户流量数据
     * @param userId 用户Id
     * @return
     */
    UserFlow getUserFlow(Long userId);

    /**
     * 更新流量(增加)
     * @param userId 用户Id
     * @param bytes 流量 kb
     * @return
     */
    boolean updateFLow(Long userId, Long bytes);

    /**
     * 判断是否有流量
     * @param userId 用户Id
     * @return
     */
    boolean hasFlow(Long userId);

    UserFlow getUserFlowAndPackageFlow(Long userId);
}
