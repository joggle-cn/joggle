package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeviceSwitchLineDTO {


    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private Long deviceId;


    private Integer serverTunnelId;

}
