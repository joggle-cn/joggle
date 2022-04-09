package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeviceMappingDelDTO {


    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private Long id;

}
