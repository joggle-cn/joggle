package com.wuweibi.bullet.service;

import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.vo.CountVO;

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
     * 统计统计数据
     * @return
     */
    UserCountVO getUserCountInfo(Long userId);

}
