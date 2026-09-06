package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DeviceCheckUpdateDTO {


    @Schema(description = "设备id")
    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private Long deviceId;



}
