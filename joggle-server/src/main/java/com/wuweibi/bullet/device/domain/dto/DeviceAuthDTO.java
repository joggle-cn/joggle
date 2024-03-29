package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeviceAuthDTO {


    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private String deviceNo;

    private String ipAddr;

    private String mac;

    private String secret;

    private String os;

    private String arch;

    private String remoteIpAddr;

    private String version;

    @ApiModelProperty("通道id")
    private Integer serverTunnelId;

}
