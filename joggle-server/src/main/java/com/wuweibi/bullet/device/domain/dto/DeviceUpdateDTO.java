package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeviceUpdateDTO {


    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private Long id;

    @NotBlank(message = "{com.wuweibi.bullet.device.name.NotBlank}")
    private String name;
}
