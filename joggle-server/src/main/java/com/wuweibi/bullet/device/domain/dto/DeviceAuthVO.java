package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DeviceAuthVO {


    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private String deviceNo;


    @Schema(description = "设备并发连接数")
    private Integer concurrentNum;
}
