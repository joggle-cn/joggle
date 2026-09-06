package com.wuweibi.bullet.domain2.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DomainSearchParam {


    @Schema(description = "搜索关键字")
    private String keyword;

    @Schema(description = "类型：1 端口 2域名")
    private Integer type;

    @Schema(description = "通道id")
    private Integer serverTunnelId;

}
