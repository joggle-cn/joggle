package com.wuweibi.bullet.dashboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTodayFlowCountVO {

    @Schema(description = "今日新增流量(MB)")
    private BigDecimal todayFlow = BigDecimal.ZERO;
    @Schema(description = "今日新增链数量")
    private Integer todayLink =  0;

}
