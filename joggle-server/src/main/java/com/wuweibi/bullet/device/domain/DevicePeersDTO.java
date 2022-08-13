package com.wuweibi.bullet.device.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * (DevicePeers)分页对象
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@SuppressWarnings("serial")
@Data
public class DevicePeersDTO {

    /**
     * id
     */
    @ApiModelProperty("id")
  	private Long id;    


    /**
     * 服务侧设备id
     */        
    @ApiModelProperty("服务侧设备id")
    @NotNull(message = "服务侧设备id不能为空")
 	private Long serverDeviceId;

    /**
     * 客户侧设备id
     */        
    @ApiModelProperty("客户侧设备id")
    @NotNull(message = "客户侧设备id不能为空")
 	private Long clientDeviceId;

    /**
     * 服务侧本地端口
     */        
    @ApiModelProperty("服务侧本地端口")
    @NotNull(message = "服务侧本地端口不能为空")
 	private Integer serverLocalPort;

    @ApiModelProperty("服务侧本地Host 默认: 0.0.0.0")
    private String serverLocalHost = "0.0.0.0";

    /**
     * 客户侧代理端口
     */        
    @ApiModelProperty("客户侧代理端口")
    @NotNull(message = "客户侧代理端口不能为空")
 	private Integer clientProxyPort;


    @ApiModelProperty("客户侧代理Host 默认: 127.0.0.1")
 	private String clientProxyHost = "127.0.0.1";

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 状态 1启用 0禁用
     */        
    @ApiModelProperty("状态 1启用 0禁用")
 	private Integer status;



}
