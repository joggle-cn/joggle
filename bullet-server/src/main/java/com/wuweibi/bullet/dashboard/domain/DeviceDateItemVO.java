package com.wuweibi.bullet.dashboard.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceDateItemVO {

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("流量MB")
    private BigDecimal flow = BigDecimal.ZERO;
}
