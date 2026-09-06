package com.wuweibi.bullet.device.domain;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备代理(DeviceProxy)分页对象
 *
 * @author marker
 * @since 2022-08-19 21:00:28
 */
@SuppressWarnings("serial")
@Data
public class DeviceProxyDTO {


    /**
     * 设备id
     */        
    @Schema(description = "设备id")
    @NotNull(message = "设备id不能空")
 	private Long deviceId;

    /**
     * 设备代理服务端口
     */        
    @Schema(description = "设备代理服务端口")
    @NotNull(message = "设备代理服务端口不能空")
 	private Integer deviceProxyPort;

    /**
     * 端口id
     */        
    @Schema(description = "端口id")
 	private Long domainId;

    /**
     * 代理协议：http/https/socks5
     */        
    @Schema(description = "代理协议：http/https/socks5")
 	private String type;


    @Schema(description = "状态 1正常 0 停用")
    private Integer status;


}
