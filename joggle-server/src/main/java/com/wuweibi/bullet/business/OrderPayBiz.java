package com.wuweibi.bullet.business;

import com.wuweibi.bullet.business.domain.OrderPayInfo;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.orders.domain.OrdersDTO;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * 订单支付的
 *
 * @author marker
 *
 */
public interface OrderPayBiz {


    /**
     * 支付宝的通知
     * @param params
     * @return
     */
    boolean aliPayNotify(Map<String, Object> params);

    /**
     * 计算价格
     * @param ordersDTO
     * @return
     */
    R<OrderPayInfo> calculate(OrdersDTO ordersDTO);


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
