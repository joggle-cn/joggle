package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

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

    @Schema(description = "通道id")
    private Integer serverTunnelId;

}
