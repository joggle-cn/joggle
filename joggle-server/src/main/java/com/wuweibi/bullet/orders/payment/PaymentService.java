package com.wuweibi.bullet.orders.payment;

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.orders.entity.Orders;


/**
 * 支付service
 * @author marker
 */
public interface PaymentService {


    /**
     * 支付宝主动查询获取回调参数
     * @param orders
     * @return
     * @throws Exception
     */
    JSONObject getAlipayCallbackParams(Orders orders) throws Exception;


    /**
     * 微信主动查询获取回调参数
     * @param orders
     * @return
     */
    JSONObject getWechatPayCallbackParams(Orders orders);
}
