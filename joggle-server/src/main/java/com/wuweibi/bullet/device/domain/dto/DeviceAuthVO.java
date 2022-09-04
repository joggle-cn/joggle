package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeviceAuthVO {


    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private String deviceNo;


}
