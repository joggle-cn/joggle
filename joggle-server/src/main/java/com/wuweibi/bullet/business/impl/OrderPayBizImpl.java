package com.wuweibi.bullet.business.impl;


import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.business.domain.OrderPayInfo;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.orders.domain.OrdersDTO;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


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


    @Override
    public R<OrderPayInfo> calculate(OrdersDTO ordersDTO) {
        Long resId = ordersDTO.getResId();
        Long amount  = ordersDTO.getAmount();
        Integer resourceType  = ordersDTO.getResourceType();

        BigDecimal payPrice = null;
        Date dueTime = null;

        OrderPayInfo orderPayInfo = new OrderPayInfo();
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



    @Override
    @Transactional
    public R balancePay(Long userId, Long domainId, BigDecimal payMoney, Long dueTime) {
        if(payMoney == null){
            return R.fail(SystemErrorType.PAY_MONEY_NOT_NULL);
        }

        // 扣减余额
        boolean status = userService.updateBalance(userId, payMoney.negate());
        if(status){
            domainService.updateDueTime(domainId, dueTime);
            return R.success();
        }else{
            return R.fail(SystemErrorType.PAY_MONEY_BALANCE_NOT_ENOUGH);
        }
    }
}
