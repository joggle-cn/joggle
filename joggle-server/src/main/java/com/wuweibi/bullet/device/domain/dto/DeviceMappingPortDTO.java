package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备端口映射
 */
@Data
public class DeviceMappingPortDTO {

    private Long id;

    /** 协议 2 TCP 5 UDP */
    @Schema(description = "协议 2 TCP 5 UDP")
    private Integer protocol;

    @Schema(description = "映射名称")
    private String name;

    @Schema(description = "目标地址 IP")
    private String host;

    @Schema(description = "目标地址端口")
    private Integer port;

    private Integer status;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "设备 id")
    private Long deviceId;
}
