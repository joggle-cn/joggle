package com.wuweibi.bullet.device.domain;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * (DeviceWhiteIps)分页对象
 *
 * @author marker
 * @since 2022-10-11 22:19:15
 */
@SuppressWarnings("serial")
@Data
public class DeviceWhiteIpsDTO {


    /**
     * 设备id
     */        
    @Schema(description = "设备id")
    @NotNull(message = "设备id不能为空")
 	private Long deviceId;

    /**
     * 分号间隔的ip地址
     */        
    @Schema(description = "分号间隔的ip地址，cidr地址")
    @NotNull(message = "ip地址不能null")
    @Size(max = 10000, message = "内容过长")
 	private String ips;

}
