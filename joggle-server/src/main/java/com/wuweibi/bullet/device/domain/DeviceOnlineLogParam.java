package com.wuweibi.bullet.device.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备在线日志(DeviceOnlineLog)分页对象
 *
 * @author marker
 * @since 2023-01-23 19:46:35
 */
@SuppressWarnings("serial")
@Data
public class DeviceOnlineLogParam {

    /**
     * id
     */
    @ApiModelProperty(value = "用户id", hidden = true)
  	private Long userId;

    /**
     * 设备id
     */        
    @ApiModelProperty(value = "设备id",required = false)
 	private Long deviceId;



}
