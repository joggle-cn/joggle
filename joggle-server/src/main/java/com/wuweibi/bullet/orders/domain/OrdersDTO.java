package com.wuweibi.bullet.orders.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrdersDTO {

    // 资源类型 1域名 2端口 3流量 4 充值 5 套餐
    @ApiModelProperty( "资源类型 1域名 2端口 3流量 4 充值 5 套餐")
    private Integer resourceType;

    @NotNull(message = "支付方式错误")
    @ApiModelProperty( "支付方式 1余额 2支付宝 3VIP权益")
    private Integer payType;

    @ApiModelProperty("数量")
    @NotNull(message = "购买数量错误")
    private Long amount;

    // 资源id
    private Long resId;

    @ApiModelProperty("用户id 不传递")
    private Long userId;
}
