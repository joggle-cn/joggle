package com.wuweibi.bullet.orders.domain;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 退款单(OrdersRefund)分页对象
 *
 * @author marker
 * @since 2022-09-19 10:31:16
 */
@SuppressWarnings("serial")
@Data
public class OrdersRefundDTO {


    /**
     * 订单id
     */        
    @Schema(description = "订单id")
    @NotNull(message = "订单id不能为空")
 	private Long orderId;

    /**
     * 退款数量
     */        
    @Schema(description = "退款数量  天  GB")
    @NotNull(message = "退款数量不能为空")
    @Min(value = 1, message = "退款数量大于0")
 	private Long amount;

    /**
     * 退款原因
     */        
    @Schema(description = "退款原因")
 	private String reason;

}
