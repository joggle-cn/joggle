package com.wuweibi.bullet.dashboard.domain;

import lombok.Data;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserCountVO {

    @Schema(description = "今日新增流量(MB)")
    private BigDecimal todayFlow = BigDecimal.ZERO;
    @Schema(description = "今日新增链数量")
    private Integer todayLink =  0;
    private BigDecimal todayFlowOn = BigDecimal.ZERO; // 同比
    private BigDecimal monthFlow = BigDecimal.ZERO;
    private BigDecimal monthFlowOn = BigDecimal.ZERO; // 同比
    private BigDecimal yearFlow = BigDecimal.ZERO;
    private BigDecimal yearFlowOn = BigDecimal.ZERO; // 同比
    private Long monthLink = 0l;
    private BigDecimal monthLinkOn = BigDecimal.ZERO; // 同比

    @Schema(description = "设备总数")
    private Integer deviceCount = 0;

    @Schema(description = "在线设备数量")
    private Integer onlineDeviceCount = 0;

    @Schema(description = "在线率(%)")
    private BigDecimal onlineRate = BigDecimal.ZERO;

    @Schema(description = "在线设备平均延迟(毫秒)")
    private Long avgLatencyMs;

}
