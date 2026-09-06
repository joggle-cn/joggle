package com.wuweibi.bullet.orders.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.config.properties.AlipayProperties;
import com.wuweibi.bullet.config.properties.WechatpayProperties;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.orders.domain.OrdersPayDTO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.service.OrdersService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * (Orders)表控制层
 *
 * @author makejava
 * @since 2022-07-21 16:16:05
 */
@Slf4j
@Tag(name = "[App]订单管理")
@WebApi
@RestController
@RequestMapping("/api/open/orders")
public class OrdersOpenController {
    /**
     * 服务对象
     */
    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderPayBiz orderPayBiz;

    @Resource
    private DomainService domainService;

    @Resource
    private Config alipayConfig;

    @Resource
    private AlipayProperties alipayProperties;
    @Resource
    private WechatpayProperties wechatpayProperties;



    /**
     * 下单接口
     *
     * @return
     */
    @PostMapping(value = "/alipay/callback", consumes = "application/x-www-form-urlencoded")
    @Transactional
    public void alipayCallback(@RequestParam HashMap params, HttpServletResponse response) throws Exception {
        Factory.setOptions(alipayConfig);
        boolean status = Factory.Payment.Common().verifyNotify(params);
        if (!status) { // 验证签名失败
            response.getWriter().write("fail");
            response.getWriter().flush();
            return;
        }
        params.put("trade_type", "NATIVE"); //扫码支付
        params.put("amount", params.get("total_amount")); // 订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位
        boolean payResult = orderPayBiz.aliPayNotify(new JSONObject(params));

        response.getWriter().write(payResult?"success":"fail");
        response.getWriter().flush();
    }


    /**
     * 阿里下单支付接口
     *
     * @return
     */
    @GetMapping(value = "/alipay")
    public void orderForAlipay(OrdersPayDTO ordersPayDTO, HttpServletResponse response) throws Exception {
        Orders orders = ordersService.getById(ordersPayDTO.getOrderId());
        if (orders == null) {
            return;
        }

        Factory.setOptions(alipayConfig);

        AlipayTradePagePayResponse result =
                Factory.Payment.Page().pay(orders.getName(), orders.getOrderNo(),
                        StringUtil.roundHalfUp(orders.getPayAmount()),
                        alipayProperties.getReturnUrl());

        // 3. 处理响应或异常
        if (ResponseChecker.success(result)) {
            log.info("支付下单成功");
        } else {
            log.error("调用失败，原因：" + result.getBody());
        }

        response.setContentType("text/html;charset=" + "utf-8");
        response.getWriter().write(result.getBody());
        response.getWriter().flush();
        response.getWriter().close();

    }
    /**
     * 微信下单支付接口
     *
     * @return
     */
    @PostMapping(value = "/wechat")
    public R orderForWechat(@RequestBody OrdersPayDTO ordersPayDTO, HttpServletResponse response1) throws Exception {
        Orders orders = ordersService.getById(ordersPayDTO.getOrderId());
        if (orders == null) {
            return R.fail("订单不存在");
        }

        com.wechat.pay.java.core.Config wechatPayConfig= SpringUtils.getBean(com.wechat.pay.java.core.Config.class);
        // 构建service
        NativePayService service = new NativePayService.Builder().config(wechatPayConfig).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(BigDecimal.valueOf(100).multiply(orders.getPayAmount()).intValue()); // 元换算分， 微信支付金额
        request.setAmount(amount);
        request.setMchid(wechatpayProperties.getMerchantId());
        request.setAppid(wechatpayProperties.getAppId());
        request.setDescription(orders.getName());
        request.setNotifyUrl(wechatpayProperties.getNotifyUrl());
        request.setOutTradeNo(orders.getOrderNo());
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        return R.ok(response.getCodeUrl());

    }



    /**
     *
     *
     * @return
     */
    @PostMapping(value = "/wechatpay/callback")
    @Transactional
    public ResponseEntity wechatpayCallback(
            @RequestHeader("Wechatpay-Signature") String signature,
            @RequestHeader("Wechatpay-Serial") String serial,
            @RequestHeader("Wechatpay-Nonce") String nonce,
            @RequestHeader("Wechatpay-Timestamp") String timestamp,
            @RequestHeader("Wechatpay-Signature-Type") String signatureType,
            @RequestBody String body, HttpServletResponse response) throws Exception {

        com.wechat.pay.java.core.Config wechatPayConfig= SpringUtils.getBean(com.wechat.pay.java.core.Config.class);
        // 构造 RequestParam
        com.wechat.pay.java.core.notification.RequestParam requestParam = new com.wechat.pay.java.core.notification.RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .signType(signatureType)
                .body(body)
                .build();
        // 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser((NotificationConfig)wechatPayConfig);
        Transaction transaction ;
        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
              transaction = parser.parse(requestParam, Transaction.class);
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            log.error("sign verification failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 转换统一参数
        JSONObject params = (JSONObject)JSON.toJSON(transaction);
        params.put("out_trade_no", params.getString("outTradeNo"));
        params.put("trade_no", params.getString("transactionId"));
        params.put("trade_type", params.getString("tradeType"));
        Amount amount = params.getObject("amount", Amount.class);
        params.put("amount",  BigDecimal.valueOf(amount.getTotal()).divide(BigDecimal.valueOf(100)));
        boolean payResult = orderPayBiz.aliPayNotify(params);
        // 如果处理失败，应返回 4xx/5xx 的状态码，例如 500 INTERNAL_SERVER_ERROR
        if (!payResult) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // 处理成功，返回 200 OK 状态码
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

