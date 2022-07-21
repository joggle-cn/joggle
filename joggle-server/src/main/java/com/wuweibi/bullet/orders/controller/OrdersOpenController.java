package com.wuweibi.bullet.orders.controller;


import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.orders.domain.OrdersPayDTO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.service.OrdersService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * (Orders)表控制层
 *
 * @author makejava
 * @since 2022-07-21 16:16:05
 */
@Slf4j
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


    /**
     * 下单接口
     * @return
     */
    @PostMapping(value = "/alipay/callback",consumes = "application/x-www-form-urlencoded")
    public void alipayCallback(@RequestParam HashMap params, HttpServletResponse response) throws Exception {
        Factory.setOptions(alipayConfig);

        boolean status = Factory.Payment.Common().verifyNotify(params);
        if(!status){ // 验证签名失败
            return;
        }
        // 成功
        String outTradeNo = (String)params.get("out_trade_no");
        String tradeNo = (String)params.get("trade_no");

        Orders orders = ordersService.getByOrderNo(outTradeNo);
        if (orders == null) {
            return;
        }

        orders.setTradeNo(tradeNo);
        orders.setPayTime(new Date());
        orders.setStatus(OrdersStatusEnum.PAYED.getStatus());
        orders.setUpdateTime(orders.getPayTime());

        ordersService.updateById(orders);


        // TODO 发放资源；




        return;

    }

    /**
     * 下单接口
     * @return
     */
    @GetMapping(value = "/alipay")
    public void create( OrdersPayDTO ordersPayDTO, HttpServletResponse response) throws Exception {
        Orders orders = ordersService.getById(ordersPayDTO.getOrderId());
        if(orders == null){
            return ;
        }

        Factory.setOptions(alipayConfig);

        AlipayTradePagePayResponse result =
                Factory.Payment.Page().pay("域名购买"+orders.getName(),orders.getOrderNo(),
                        StringUtil.roundHalfUp(orders.getPayAmount()),
                        "http://localhost:63342/");

        // 3. 处理响应或异常
        if (ResponseChecker.success(result)) {
            log.info("支付下单成功");
        } else {
            log.error("调用失败，原因：" + result.getBody());
        }

        response.setContentType("text/html;charset="  + "utf-8");
        response.getWriter().write(result.getBody());
        response.getWriter().flush();
        response.getWriter().close();

        return;
    }







    }

