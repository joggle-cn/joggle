package com.wuweibi.bullet.business;

import com.wuweibi.bullet.entity.api.Result;

/**
 *
 * 订单支付的
 *
 * @author marker
 *
 */
public interface OrderPayBiz {

    /**
     * 计算价格
     * @param domainId
     * @param time 天
     * @return
     */
    Result calculate(Long domainId, Integer time);
}
