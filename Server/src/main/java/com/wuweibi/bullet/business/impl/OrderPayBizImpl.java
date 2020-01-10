package com.wuweibi.bullet.business.impl;


import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wuweibi.bullet.business.OrderPayBiz;

import java.math.BigDecimal;
import java.util.Calendar;

import static com.wuweibi.bullet.builder.MapBuilder.newMap;


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
    @Autowired
    private DomainService domainService;




    @Override
    public Result calculate(Long domainId, Integer time) {
        Domain domain = domainService.selectById(domainId);
        // 校验域名是否存在
        if(domain == null){
            return Result.fail();
        }

        if(domain.getDueTime() == null){
            return Result.fail();
        }

        // 计算价格
        BigDecimal price = domain.getSalesPrice();
        BigDecimal payPrice = price.multiply(BigDecimal.valueOf(time));

        // 计算到期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(domain.getDueTime());
        calendar.add(Calendar.DATE, time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);



        return Result.success(newMap(2)
                .setParam("payMoney",  StringUtil.roundHalfUp(payPrice))
                .setParam("dueTime",  calendar.getTime().getTime())
                .build());
    }
}
