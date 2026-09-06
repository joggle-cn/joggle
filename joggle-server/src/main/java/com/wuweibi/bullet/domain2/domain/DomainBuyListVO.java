package com.wuweibi.bullet.domain2.domain;

import lombok.Data;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DomainBuyListVO {

    @Schema(description = "domainId")
    private Long id;
    @Schema(description = "二级域名前缀或端口")
    private String domain;
    @Schema(description = "类型： 1 端口 2域名")
    private Integer type;
    @Schema(description = "类型名称")
    private String typeName;
    @Schema(description = "域名状态 ：1已售、0释放、-1 禁售")
    private Integer status;
    @Schema(description = "原价格")
    private BigDecimal originalPrice;
    @Schema(description = "销售价格")
    private BigDecimal salesPrice;

    @Schema(description = "通道区域")
    private String tunnelName;
    @Schema(description = "通道地区")
    private String tunnelArea;
    @Schema(description = "通道国家")
    private String tunnelCountry;
    @Schema(description = "通道宽带")
    private Integer tunnelBroadband;

    @Schema(description = "宽带峰值 mbps")
    private Integer bandwidth;
}
