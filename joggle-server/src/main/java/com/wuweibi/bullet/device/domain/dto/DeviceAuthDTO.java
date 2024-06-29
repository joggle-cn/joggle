package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeviceAuthDTO {


    @NotNull
    private String deviceNo;

    private String ipAddr;

    private String mac;

    private String secret;

    @NotBlank
    private String os;

    @NotBlank
    private String arch;

    @NotBlank
    private String remoteIpAddr;

    @NotBlank
    private String version;

    @ApiModelProperty("通道id")
    private Integer serverTunnelId;

}
