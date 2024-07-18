package com.wuweibi.bullet.dashboard.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserFlowCountDTO {

    private BigDecimal todayFlow = BigDecimal.ZERO;
    // 昨日流量
    private BigDecimal todayFlow2 = BigDecimal.ZERO;
    private BigDecimal todayFlowOn = BigDecimal.ZERO; // 同比
    private BigDecimal monthFlow = BigDecimal.ZERO;
    private BigDecimal monthFlowOn = BigDecimal.ZERO; // 同比
    private BigDecimal yearFlow = BigDecimal.ZERO;
    private BigDecimal yearFlowOn = BigDecimal.ZERO; // 同比
    private Long monthLink = 0l;
    private BigDecimal monthLinkOn = BigDecimal.ZERO; // 同比

}
