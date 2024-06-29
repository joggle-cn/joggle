package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ServerTunnelParam {

    // 通道名称
    @ApiModelProperty("通道名称")
    private String name;

    @ApiModelProperty("用户id")
    private Long userId;




}
