package com.wuweibi.bullet.dashboard.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTodayFlowCountVO {

    @ApiModelProperty("今日新增流量(MB)")
    private BigDecimal todayFlow = BigDecimal.ZERO;
    @ApiModelProperty("今日新增链数量")
    private Integer todayLink =  0;

}
