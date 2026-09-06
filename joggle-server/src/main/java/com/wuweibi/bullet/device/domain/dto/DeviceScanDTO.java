package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class DeviceScanDTO {


    @NotNull
    private String deviceNo;

}
