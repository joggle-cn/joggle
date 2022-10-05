package com.wuweibi.bullet.orders.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 退款单(OrdersRefund)分页对象
 *
 * @author marker
 * @since 2022-09-19 10:31:16
 */
@SuppressWarnings("serial")
@Data
public class RefundAuditDTO {


    /**
     * 订单id
     */        
    @ApiModelProperty("订单id")
    @NotNull(message = "订单id不能为空")
 	private Long orderId;

    /**
     * 退款数量
     */        
    @ApiModelProperty("退款数量  天  GB")
    @NotNull(message = "退款数量不能为空")
 	private Long status;

    /**
     * 退款原因
     */        
    @ApiModelProperty("退款原因")
 	private String reason;

}
