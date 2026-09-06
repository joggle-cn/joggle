package com.wuweibi.bullet.orders.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class OrdersConfirmDTO {


    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;


}
