package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class ServerTunnelParam {

    // 通道名称
    @Schema(description = "通道名称")
    private String name;

    @Schema(description = "用户id")
    private Long userId;




}
