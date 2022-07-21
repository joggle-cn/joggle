package com.wuweibi.bullet.business.impl;


import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.business.domain.OrderPayInfo;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.orders.domain.OrdersDTO;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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




    @Override
    public R<OrderPayInfo> calculate(OrdersDTO ordersDTO) {
        Long resId = ordersDTO.getResId();
        Integer time  = ordersDTO.getTime();
        Integer resourceType  = ordersDTO.getResourceType();

        BigDecimal payPrice = null;
        Date dueTime = null;

        OrderPayInfo orderPayInfo = new OrderPayInfo();
        switch (resourceType){
            case  1:
            case  2: // 域名
                Domain domain = domainService.getById(resId);
                // 校验域名是否存在
                if(domain == null){
                    return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
                }
                if (domain.getDueTime() == null) {
                    domain.setDueTime(new Date());
                }

                // 计算价格 单价 * 天数
                BigDecimal price = domain.getSalesPrice();
                BigDecimal payAmount = price.multiply(BigDecimal.valueOf(time));
                BigDecimal originalAmount = domain.getOriginalPrice().multiply(BigDecimal.valueOf(time));

                // 计算到期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(domain.getDueTime());
                calendar.add(Calendar.DATE, time);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                dueTime = calendar.getTime();
                orderPayInfo.setDueTime(dueTime.getTime());
                orderPayInfo.setAmount(time*24*60*60l);
                orderPayInfo.setName(domain.getDomain());
                orderPayInfo.setDiscountAmount(BigDecimal.ZERO);
                orderPayInfo.setPriceAmount(originalAmount);
                orderPayInfo.setPayAmount(payAmount);
                break;
            case 3:
                // 查询流量价格套餐
                BigDecimal priceAmount = BigDecimal.valueOf(2);

                orderPayInfo.setName("流量套餐");
                orderPayInfo.setPriceAmount(priceAmount);
                orderPayInfo.setPayAmount(priceAmount);
                orderPayInfo.setDiscountAmount(BigDecimal.ZERO);
                orderPayInfo.setAmount(01l);


        }

        return R.success(orderPayInfo);
    }

    @Autowired
    private UserService userService;


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
