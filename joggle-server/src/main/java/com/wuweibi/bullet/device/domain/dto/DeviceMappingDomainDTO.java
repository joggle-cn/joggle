package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备域名映射
 *
 * @author marker
 * @date 2022/11/21 16:45
 */
@Data
public class DeviceMappingDomainDTO {

    private Long id;

    /** 协议 1 HTTP 3 HTTPS 4 HTTP/HTTPS */
    @ApiModelProperty(value = "协议 1 HTTP 3 HTTPS 4 HTTP/HTTPS")
    private Integer protocol;

    @ApiModelProperty(value = "目标地址 IP")
    private String host;

    @ApiModelProperty(value = "映射名称")
    private String name;

    @ApiModelProperty(value = "目标地址端口")
    private Integer port;

    @ApiModelProperty(value = "用户自定义域名")
    private Long userDomainId;

    @ApiModelProperty(value = "映射状态")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "设备 id")
    private Long deviceId;
}
