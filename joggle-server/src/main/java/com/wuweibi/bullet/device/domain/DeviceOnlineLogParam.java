package com.wuweibi.bullet.device.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "用户id", hidden = true)
  	private Long userId;

    /**
     * 设备id
     */        
    @Schema(description = "设备id", required = false)
 	private Long deviceId;



}
