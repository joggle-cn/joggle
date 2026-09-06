package com.wuweibi.bullet.orders.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class OrdersAdminParam {

    @Schema(description = "资源类型 1域名 2端口 3流量 4 充值")
    private Integer resourceType;

    @Schema(description = "支付方式 1余额 2支付宝")
    private Integer payType;

    @Schema(description = "状态  0待支付 1已支付 2 取消 3退款中 4已退款")
    private Integer status;
    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户id")
    private Long userId;
}
