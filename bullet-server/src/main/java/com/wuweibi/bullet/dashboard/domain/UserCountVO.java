package com.wuweibi.bullet.dashboard.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCountVO {

    private Long todayFlow;
    private BigDecimal todayFlowOn; // 同比
    private Long monthFlow;
    private BigDecimal monthFlowOn; // 同比
    private Long yearFlow;
    private BigDecimal yearFlowOn; // 同比
    private Long monthLink;
    private BigDecimal monthLinkOn; // 同比

}
