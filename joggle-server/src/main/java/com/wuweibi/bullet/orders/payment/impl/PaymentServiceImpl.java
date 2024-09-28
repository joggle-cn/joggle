package com.wuweibi.bullet.orders.payment.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import com.wuweibi.bullet.config.properties.WechatpayProperties;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.payment.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {



    @Resource
    private Config alipayConfig;


    @Resource
    private WechatpayProperties wechatpayProperties;

    @Resource
    private com.wechat.pay.java.core.Config wechatPayConfig;


    @Override
    public JSONObject getAlipayCallbackParams(Orders orders) throws Exception {
        JSONObject params = new JSONObject(5);
        // 主动查询订单支付状态
        Factory.setOptions(alipayConfig);
        AlipayTradeQueryResponse result =
                Factory.Payment.Common().query(orders.getOrderNo());

        if (ResponseChecker.success(result)) {
            // 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
            // {"alipay_trade_query_response":{"code":"10000","msg":"Success","buyer_logon_id":"gln***@sandbox.com","buyer_pay_amount":"0.00","buyer_user_id":"2088622987384692","buyer_user_type":"PRIVATE","invoice_amount":"0.00","out_trade_no":"999809380105","point_amount":"0.00","receipt_amount":"0.00","send_pay_date":"2022-07-21 23:23:03","total_amount":"70.00","trade_no":"2022072122001484690502290245","trade_status":"TRADE_SUCCESS"},"sign":"OP2QmZfPi25jL9ShqZWvssnzTbptPUFSWXiM5SanfetE7uruZyJDW+rR8Jyw/71OWdDRoUVF9fmKJsTak9lu2UHcLg8rLBpYfJ1Qlex2lUgINHfN2X+dBGFMwQ+y3hk0SfnzY34wNAnbuaeGPPzgmbv7KE16Rjw3mzVICBJUiPO+M5g4dgXw+lbEdRK30wGEYi4gbcN4lGYqUrOAN5DJ8Sl7ovaQ9yce2oE/gTu/NPHElYihUK8Ln1M4KDdK/mtTgEH2tiwtV7QsMaYD9ydkx5rCw78KsPnZPAdVmMPbVfg28cZI0ialk0EAjgPM/Of9LXfAFpVn86/wdhiHtvz2eA=="}
            if ("TRADE_SUCCESS".equals(result.getTradeStatus())) {// 支付成功
                params.put("out_trade_no", result.getOutTradeNo());
                params.put("trade_no", result.getTradeNo());
                params.put("trade_status", "SUCCESS"); // 交易状态
                params.put("trade_type", "NATIVE"); //扫码支付
                params.put("amount", result.getPayAmount()); // 订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位
            }
        } else {
            log.error("调用失败，原因：" + result.getBody());
        }
        return params;
    }

    @Override
    public JSONObject getWechatPayCallbackParams(Orders orders) {
        // 构造 RequestParam
        com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
        queryRequest.setMchid(wechatpayProperties.getMerchantId());
        queryRequest.setOutTradeNo(orders.getOrderNo());

        // 构建service
        NativePayService service = new NativePayService.Builder().config(wechatPayConfig).build();
        Transaction transaction = service.queryOrderByOutTradeNo(queryRequest);

        // 转换统一参数
        JSONObject params = (JSONObject) JSON.toJSON(transaction);
        params.put("out_trade_no", transaction.getOutTradeNo());
        params.put("trade_no", transaction.getTransactionId());
        params.put("trade_type", params.getString("tradeType"));
        // 交易状态，枚举值：SUCCESS：支付成功REFUND：转入退款NOTPAY：未支付CLOSED：已关闭REVOKED：已撤销（付款码支付）USERPAYING：用户支付中（付款码支付）PAYERROR：支付失败(其他原因，如银行返回失败)
        params.put("trade_status", transaction.getTradeState().name()); // 支付状态
        Amount amount = params.getObject("amount", Amount.class);
        params.put("amount",  BigDecimal.valueOf(amount.getTotal()).divide(BigDecimal.valueOf(100)));

        return params;
    }
}
