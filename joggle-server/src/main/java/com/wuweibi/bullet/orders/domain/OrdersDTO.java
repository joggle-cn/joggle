package com.wuweibi.bullet.orders.domain;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class OrdersDTO {

    @Schema(description = "资源类型 1端口 2域名 3流量 4 充值 5 套餐")
    private Integer resourceType;

    @NotNull(message = "支付方式错误")
    @Schema(description = "支付方式 1余额 2支付宝 3VIP权益 4微信支付")
    private Integer payType;

    @Schema(description = "数量")
    @NotNull(message = "购买数量错误")
    private Long amount;

    // 资源id
    private Long resId;

    @Schema(description = "用户id 不传递")
    private Long userId;
}
