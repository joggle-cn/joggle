package com.wuweibi.bullet.device.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

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
    @ApiModelProperty("设备id")
    @NotNull(message = "设备id不能空")
 	private Long deviceId;

    /**
     * 设备代理服务端口
     */        
    @ApiModelProperty("设备代理服务端口")
    @NotNull(message = "设备代理服务端口不能空")
 	private Integer deviceProxyPort;

    /**
     * 端口id
     */        
    @ApiModelProperty("端口id")
 	private Long domainId;

    /**
     * 代理协议：http/https/socks5
     */        
    @ApiModelProperty("代理协议：http/https/socks5")
 	private String type;


    @ApiModelProperty("状态 1正常 0 停用")
    private Integer status;


}
