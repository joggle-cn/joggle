package com.wuweibi.bullet.dashboard.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCountVO {

    private Long todayFlow = 0l;
    private BigDecimal todayFlowOn = BigDecimal.ZERO; // 同比
    private Long monthFlow = 0l;
    private BigDecimal monthFlowOn = BigDecimal.ZERO; // 同比
    private Long yearFlow = 0l;
    private BigDecimal yearFlowOn = BigDecimal.ZERO; // 同比
    private Long monthLink = 0l;
    private BigDecimal monthLinkOn = BigDecimal.ZERO; // 同比

}
