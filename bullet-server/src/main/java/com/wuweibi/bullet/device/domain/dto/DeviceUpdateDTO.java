package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeviceUpdateDTO {


    private Long id;

    @NotBlank(message = "{com.wuweibi.bullet.device.name.NotBlank}")
    private String name;
}
