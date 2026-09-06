package com.wuweibi.bullet.orders.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class OrdersParam {

    //资源类型 1域名 2端口 3流量 4 充值
    private Integer resourceType;

    //支付方式 1余额 2支付宝
    private Integer payType;

    @Schema(description = "数量")
    private Long amount;

    // 资源id
    private Long resId;

    private Long userId;
}
