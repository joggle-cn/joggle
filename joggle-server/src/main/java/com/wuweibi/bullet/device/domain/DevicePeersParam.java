package com.wuweibi.bullet.device.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (DevicePeers)分页对象
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@SuppressWarnings("serial")
@Data
public class DevicePeersParam {



    /**
     * 用户id
     */        
    @ApiModelProperty("用户id")
 	private Long userId;

    /**
     * 服务侧设备id
     */        
    @ApiModelProperty("服务侧设备id")
 	private Long serverDeviceId;

    /**
     * 客户侧设备id
     */        
    @ApiModelProperty("客户侧设备id")
 	private Long clientDeviceId;

    /**
     * 状态 1启用 0禁用
     */        
    @ApiModelProperty("状态 1启用 0禁用")
 	private Integer status;



}
