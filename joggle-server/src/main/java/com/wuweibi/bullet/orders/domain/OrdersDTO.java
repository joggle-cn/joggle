package com.wuweibi.bullet.orders.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrdersDTO {

    //资源类型 1域名 2端口 3流量 4 充值
    private Integer resourceType;

    //支付方式 1余额 2支付宝
    @NotNull(message = "支付方式错误")
    private Integer payType;

    @ApiModelProperty("数量")
    @NotNull(message = "购买数量错误")
    private Long amount;

    // 资源id
    private Long resId;

    @ApiModelProperty("用户id 不传递")
    private Long userId;
}
