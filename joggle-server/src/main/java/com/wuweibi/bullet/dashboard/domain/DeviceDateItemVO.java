package com.wuweibi.bullet.dashboard.domain;

import lombok.Data;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DeviceDateItemVO {

    @Schema(description = "时间 yyyy-MM-dd")
    private String time;

    @Schema(description = "全部流量MB")
    private BigDecimal flow = BigDecimal.ZERO;

    @Schema(description = "入网流量MB")
    private BigDecimal flowIn = BigDecimal.ZERO;

    @Schema(description = "出网流量MB")
    private BigDecimal flowOut = BigDecimal.ZERO;

    @Schema(description = "链接数量")
    private BigDecimal link = BigDecimal.ZERO;

}
