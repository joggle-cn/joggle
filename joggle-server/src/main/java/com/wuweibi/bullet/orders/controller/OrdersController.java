package com.wuweibi.bullet.orders.controller;


import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.business.domain.OrderPayInfo;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.orders.domain.OrdersConfirmDTO;
import com.wuweibi.bullet.orders.domain.OrdersDTO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.service.OrdersService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.CodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * (Orders)表控制层
 *
 * @author makejava
 * @since 2022-07-21 16:16:05
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    /**
     * 服务对象
     */
    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderPayBiz orderPayBiz;

    @Resource
    private DomainService domainService;


    /**
     * 计算价格
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public Object calculate(@JwtUser Session session, @RequestBody OrdersDTO ordersDTO) {
        Long userId = session.getUserId();
        ordersDTO.setUserId(userId);

        // 检查是否该用户的域名
//        if(!domainService.checkDomain(userId, domainId)){
//            return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
//        }
        return orderPayBiz.calculate(ordersDTO);
    }

    @Resource
    private Config alipayConfig;


    /**
     * 下单接口
     *
     * @param session
     * @return
     */
    @PostMapping(value = "/create")
    @Transactional
    public R create(@JwtUser Session session, @RequestBody OrdersDTO ordersDTO) throws Exception {
        Long userId = session.getUserId();
        ordersDTO.setUserId(userId);
        // 校验数据


        // 计算价格
        R<OrderPayInfo> r = orderPayBiz.calculate(ordersDTO);
        if (r.isFail()) {
            return r;
        }
        OrderPayInfo orderPayInfo = r.getData();

        Orders orders = new Orders();

        // 订单号生成
        long id = CodeHelper.getIdWorker().nextId();
        orders.setOrderNo(String.valueOf(id));
        orders.setName(orderPayInfo.getName());
        orders.setAmount(orderPayInfo.getAmount());
        orders.setPayType(ordersDTO.getPayType());
        orders.setUserId(userId);
        orders.setResourceType(ordersDTO.getResourceType());
        orders.setDomainId(ordersDTO.getResId());
        orders.setPayAmount(orderPayInfo.getPayAmount());
        orders.setDiscountAmount(orderPayInfo.getDiscountAmount());
        orders.setPriceAmount(orderPayInfo.getPriceAmount());
        orders.setStatus(OrdersStatusEnum.WAIT_PAY.getStatus());
        orders.setCreateTime(new Date());
        orders.setUpdateTime(orders.getCreateTime());

        ordersService.save(orders);


        switch (orders.getResourceType()) {
            case 1: // 域名
            case 2: // 端口
                Domain domain = domainService.getById(orders.getDomainId());
                // 校验域名是否存在
                if (domain == null) {
                    return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
                }
                domainService.updateUserId(orders.getDomainId(), orders.getUserId());
                break;
        }


        return R.success(orders.getId());
    }


    /**
     * 下单接口
     *
     * @param session
     * @return
     */
    @PostMapping(value = "/confirm")
    public R confirm(@JwtUser Session session, @RequestBody @Valid OrdersConfirmDTO dto) throws Exception {
        Long userId = session.getUserId();
        // 校验数据

        Orders orders = ordersService.getById(dto.getOrderId());
        if (orders == null) {
            return R.fail("订单不存在");
        }
        if (orders.getStatus() == OrdersStatusEnum.WAIT_PAY.getStatus()) {
            // 主动查询订单支付状态
            Factory.setOptions(alipayConfig);
            AlipayTradeQueryResponse result =
                    Factory.Payment.Common().query(orders.getOrderNo());

            if (ResponseChecker.success(result)) {
                // {"alipay_trade_query_response":{"code":"10000","msg":"Success","buyer_logon_id":"gln***@sandbox.com","buyer_pay_amount":"0.00","buyer_user_id":"2088622987384692","buyer_user_type":"PRIVATE","invoice_amount":"0.00","out_trade_no":"999809380105","point_amount":"0.00","receipt_amount":"0.00","send_pay_date":"2022-07-21 23:23:03","total_amount":"70.00","trade_no":"2022072122001484690502290245","trade_status":"TRADE_SUCCESS"},"sign":"OP2QmZfPi25jL9ShqZWvssnzTbptPUFSWXiM5SanfetE7uruZyJDW+rR8Jyw/71OWdDRoUVF9fmKJsTak9lu2UHcLg8rLBpYfJ1Qlex2lUgINHfN2X+dBGFMwQ+y3hk0SfnzY34wNAnbuaeGPPzgmbv7KE16Rjw3mzVICBJUiPO+M5g4dgXw+lbEdRK30wGEYi4gbcN4lGYqUrOAN5DJ8Sl7ovaQ9yce2oE/gTu/NPHElYihUK8Ln1M4KDdK/mtTgEH2tiwtV7QsMaYD9ydkx5rCw78KsPnZPAdVmMPbVfg28cZI0ialk0EAjgPM/Of9LXfAFpVn86/wdhiHtvz2eA=="}
                if ("TRADE_SUCCESS".equals(result.getTradeStatus())) {// 支付成功
                    Map<String, Object> params = new HashMap<>(3);
                    params.put("out_trade_no", result.getOutTradeNo());
                    params.put("trade_no", result.getTradeNo());
                    params.put("trade_status", result.getTradeStatus());

                    boolean payResult = orderPayBiz.aliPayNotify(params);
                    if (!payResult) {
                        return R.fail("还收到支付通知");
                    }
                }
            } else {
                log.error("调用失败，原因：" + result.getBody());
            }
            return R.fail("订单未查询到支付信息");
        }
        return R.success();
    }


}
