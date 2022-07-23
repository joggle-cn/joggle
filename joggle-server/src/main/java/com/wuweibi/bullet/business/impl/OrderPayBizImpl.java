package com.wuweibi.bullet.business.impl;


import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.business.domain.OrderPayInfo;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.BaseException;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.orders.domain.OrdersDTO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.service.OrdersService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.utils.SpringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * 订单支付的
 *
 * @author marker
 *
 */
@Service
public class OrderPayBizImpl implements OrderPayBiz {


    /** 域名管理 */
    @Resource
    private DomainService domainService;


    @Resource
    private UserService userService;

    /**
     * 服务对象
     */
    @Resource
    private OrdersService ordersService;


    @Resource
    private UserFlowService userFlowService;



    @Override
    public R<OrderPayInfo> calculate(OrdersDTO ordersDTO) {
        Long resId = ordersDTO.getResId();
        Long amount  = ordersDTO.getAmount();
        Integer resourceType  = ordersDTO.getResourceType();

        BigDecimal payPrice = null;
        Date dueTime = null;

        OrderPayInfo orderPayInfo = new OrderPayInfo();
        orderPayInfo.setPayType(ordersDTO.getPayType());
        switch (resourceType){
            case 1: // 域名
            case 2: // 端口
                Domain domain = domainService.getById(resId);
                // 校验域名是否存在
                if(domain == null){
                    return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
                }
                if(!(domain.getUserId() != null && ordersDTO.getUserId().equals(domain.getUserId()))){
                    return R.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
                }
                if (domain.getDueTime() == null) {
                    domain.setDueTime(new Date());
                }

                // 计算价格 单价 * 天数
                BigDecimal price = domain.getSalesPrice();
                BigDecimal payAmount = price.multiply(BigDecimal.valueOf(amount));
                BigDecimal originalAmount = domain.getOriginalPrice().multiply(BigDecimal.valueOf(amount));

                // 计算到期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(domain.getDueTime());
                calendar.add(Calendar.DATE, amount.intValue());
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                dueTime = calendar.getTime();
                orderPayInfo.setDueTime(dueTime.getTime());
                orderPayInfo.setAmount(amount*24*60*60l);
                orderPayInfo.setName(domain.getDomain());
                orderPayInfo.setDiscountAmount(BigDecimal.ZERO);
                orderPayInfo.setPriceAmount(originalAmount);
                orderPayInfo.setPayAmount(payAmount);
                break;
            case 3:
                // 查询流量价格套餐 1.6元
                BigDecimal priceAmount = BigDecimal.valueOf(1.6);
                payAmount = priceAmount.multiply(BigDecimal.valueOf(amount));

                orderPayInfo.setName("流量套餐");
                orderPayInfo.setPriceAmount(payAmount);
                orderPayInfo.setPayAmount(payAmount);
                orderPayInfo.setDiscountAmount(BigDecimal.ZERO);
                orderPayInfo.setAmount(amount);
                break;
        }

        return R.success(orderPayInfo);
    }


    /**
     * 支付宝 通知
     * @param params 回调参数
     * @return
     */
    @Transactional
    public boolean aliPayNotify(Map<String, Object> params) {
        // 成功
        String outTradeNo = (String) params.get("out_trade_no");
        String tradeNo = (String) params.get("trade_no");

        Orders orders = ordersService.getByOrderNo(outTradeNo);
        if (orders == null) {
            return false;
        }

        orders.setTradeNo(tradeNo);
        orders.setPayTime(new Date());
        orders.setStatus(OrdersStatusEnum.PAYED.getStatus());
        orders.setUpdateTime(orders.getPayTime());

        ordersService.updateById(orders);

        // 发放资源；
        switch (orders.getResourceType()) {
            case 1: // 域名
            case 2: // 端口
                Domain domain = domainService.getById(orders.getDomainId());
                // 校验域名是否存在
                if (domain == null) {
                    throw new RuntimeException("校验域名是否存在");
                }

                // 计算到期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(domain.getDueTime());
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.add(Calendar.SECOND, orders.getAmount().intValue());
                domainService.updateDueTime(orders.getDomainId(), calendar.getTime().getTime());
                break;
            case 3: // 流量
                long flow = orders.getAmount() * 1024 * 1024;
                userFlowService.updateFLow(orders.getUserId(), flow);
                break;
        }
        return true;
    }


    @Override
    @Transactional
    public R balancePay(Long userId, BigDecimal payMoney, String orderNo) {
        if(payMoney == null){
            return R.fail(SystemErrorType.PAY_MONEY_NOT_NULL);
        }

        // 扣减余额
        boolean status = userService.updateBalance(userId, payMoney.negate());
        if (status) {
            OrderPayBiz orderPayBiz = SpringUtils.getBean(OrderPayBiz.class);
            Map<String, Object> params = new HashMap<>(3);
            params.put("out_trade_no", orderNo);
            params.put("trade_no", "余额流水号");
            params.put("trade_status", "TRADE_SUCCESS");
            boolean payStatus = orderPayBiz.aliPayNotify(params);
            if (!payStatus) {
                throw new BaseException("产品发放失败!");
            }
            return R.success();
        } else {
            return R.fail(SystemErrorType.PAY_MONEY_BALANCE_NOT_ENOUGH);
        }
    }
}
