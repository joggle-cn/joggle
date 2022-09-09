package com.wuweibi.bullet.domain2.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DomainBuyListVO {

    @ApiModelProperty("domainId")
    private Long id;
    @ApiModelProperty("二级域名前缀或端口")
    private String domain;
    @ApiModelProperty("类型： 1 端口 2域名")
    private Integer type;
    @ApiModelProperty("类型名称")
    private String typeName;
    @ApiModelProperty("域名状态 ：1已售、0释放、-1 禁售")
    private Integer status;
    @ApiModelProperty("原价格")
    private BigDecimal originalPrice;
    @ApiModelProperty("销售价格")
    private BigDecimal salesPrice;

    @ApiModelProperty("通道区域")
    private String tunnelName;
    @ApiModelProperty("通道地区")
    private String tunnelArea;
    @ApiModelProperty("通道国家")
    private String tunnelCountry;
    @ApiModelProperty("通道宽带")
    private Integer tunnelBroadband;
}
