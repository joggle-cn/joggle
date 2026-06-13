package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备端口映射
 */
@Data
public class DeviceMappingPortDTO {

    private Long id;

    /** 协议 2 TCP 5 UDP */
    @ApiModelProperty(value = "协议 2 TCP 5 UDP")
    private Integer protocol;

    @ApiModelProperty(value = "映射名称")
    private String name;

    @ApiModelProperty(value = "目标地址 IP")
    private String host;

    @ApiModelProperty(value = "目标地址端口")
    private Integer port;

    private Integer status;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "设备 id")
    private Long deviceId;
}
