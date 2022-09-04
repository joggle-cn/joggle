package com.wuweibi.bullet.domain2.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DomainSearchParam {


    @ApiModelProperty("搜索关键字")
    private String keyword;

    @ApiModelProperty("类型：1 端口 2域名")
    private Integer type;

    @ApiModelProperty("通道id")
    private Integer serverTunnelId;

}
