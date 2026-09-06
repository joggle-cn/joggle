package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class DeviceServiceStatusDTO {


    @NotNull
    private Long serviceId; // 服务ID(t_device_mapping.id)

}
