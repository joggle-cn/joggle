package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeviceCheckUpdateDTO {


    @ApiModelProperty("设备id")
    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private Long deviceId;



}
