package com.wuweibi.bullet.device.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "用户id")
 	private Long userId;

    /**
     * 服务侧设备id
     */        
    @Schema(description = "服务侧设备id")
 	private Long serverDeviceId;

    /**
     * 客户侧设备id
     */        
    @Schema(description = "客户侧设备id")
 	private Long clientDeviceId;

    /**
     * 状态 1启用 0禁用
     */        
    @Schema(description = "状态 1启用 0禁用")
 	private Integer status;



}
