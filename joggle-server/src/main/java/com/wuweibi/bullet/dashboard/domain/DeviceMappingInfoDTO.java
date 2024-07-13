package com.wuweibi.bullet.dashboard.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceMappingInfoDTO {

    @ApiModelProperty("设备ID")
    private Integer deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备下的映射Id集合")
    private String mappingIds;

    @ApiModelProperty("设备流量MB")
    private BigDecimal flow = BigDecimal.ZERO;
}
