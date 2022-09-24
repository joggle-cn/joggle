package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeviceAuthVO {


    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private String deviceNo;


    @ApiModelProperty("设备并发连接数")
    private Integer concurrentNum;
}
