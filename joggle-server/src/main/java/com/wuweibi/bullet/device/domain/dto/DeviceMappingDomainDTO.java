package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "协议 1 HTTP 3 HTTPS 4 HTTP/HTTPS")
    private Integer protocol;

    @Schema(description = "目标地址 IP")
    private String host;

    @Schema(description = "映射名称")
    private String name;

    @Schema(description = "目标地址端口")
    private Integer port;

    @Schema(description = "用户自定义域名")
    private Long userDomainId;

    @Schema(description = "映射状态")
    private Integer status;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "设备 id")
    private Long deviceId;
}
