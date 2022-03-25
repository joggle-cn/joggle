package com.wuweibi.bullet.business;

import com.wuweibi.bullet.entity.api.R;

import java.math.BigDecimal;

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
    R calculate(Long domainId, Integer time);


    /**
     * 余额支付
     * @param userId
     * @param domainId
     * @param payMoney
     * @param dueTime
     * @return
     */
    R balancePay(Long userId, Long domainId, BigDecimal payMoney, Long dueTime);

}
