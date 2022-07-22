package com.wuweibi.bullet.orders.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrdersConfirmDTO {


    @ApiModelProperty("订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;


}
