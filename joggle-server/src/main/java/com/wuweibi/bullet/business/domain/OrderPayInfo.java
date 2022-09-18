package com.wuweibi.bullet.business.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderPayInfo {

    private BigDecimal payAmount;
    // 优惠金额
    private BigDecimal discountAmount;
    // 原价
    private BigDecimal priceAmount;

    /**
     * 购买的时长(天)，购买的流量
     */
    private Long amount;


    /**
     * 购买的时长(秒)，购买的流量
     */
    private Long realAmount;

    @ApiModelProperty("购买后的实效时间")
    private Long dueTime;

    private String name;

    private Integer payType;
    private Integer resourceType;


    @ApiModelProperty("通道服务器到期时间")
    private Date serverEndTime;
}
