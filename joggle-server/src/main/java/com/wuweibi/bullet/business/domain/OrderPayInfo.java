package com.wuweibi.bullet.business.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPayInfo {

    private BigDecimal payAmount;
    // 优惠金额
    private BigDecimal discountAmount;
    // 原价
    private BigDecimal priceAmount;

    /**
     * 购买的时长，购买的流量
     */
    private Long amount;
    private Long dueTime;
    private String name;
}
