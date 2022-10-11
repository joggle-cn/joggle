package com.wuweibi.bullet.device.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @ApiModelProperty("设备id")
    @NotNull(message = "设备id不能为空")
 	private Long deviceId;

    /**
     * 分号间隔的ip地址
     */        
    @ApiModelProperty("分号间隔的ip地址，cidr地址")
    @NotNull(message = "ip地址不能null")
    @Size(max = 10000, message = "内容过长")
 	private String ips;

}
