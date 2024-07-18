package com.wuweibi.bullet.dashboard.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCountVO {

    @ApiModelProperty("今日新增流量(MB)")
    private BigDecimal todayFlow = BigDecimal.ZERO;
    @ApiModelProperty("今日新增链数量")
    private Integer todayLink =  0;
    private BigDecimal todayFlowOn = BigDecimal.ZERO; // 同比
    private BigDecimal monthFlow = BigDecimal.ZERO;
    private BigDecimal monthFlowOn = BigDecimal.ZERO; // 同比
    private BigDecimal yearFlow = BigDecimal.ZERO;
    private BigDecimal yearFlowOn = BigDecimal.ZERO; // 同比
    private Long monthLink = 0l;
    private BigDecimal monthLinkOn = BigDecimal.ZERO; // 同比

}
