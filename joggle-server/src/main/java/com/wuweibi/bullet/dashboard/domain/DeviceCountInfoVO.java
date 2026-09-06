package com.wuweibi.bullet.dashboard.domain;

import lombok.Data;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DeviceCountInfoVO {

    @Schema(description = "设备ID")
    private Integer deviceId;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "设备流量MB")
    private BigDecimal flow = BigDecimal.ZERO;
}
