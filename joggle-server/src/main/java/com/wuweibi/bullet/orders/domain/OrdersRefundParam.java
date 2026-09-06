package com.wuweibi.bullet.orders.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 退款单(OrdersRefund)分页对象
 *
 * @author marker
 * @since 2022-09-19 10:31:16
 */
@SuppressWarnings("serial")
@Data
public class OrdersRefundParam {


    /**
     * 订单id
     */        
    @Schema(description = "订单id")
 	private Long orderId;

    /**
     * 用户id
     */        
    @Schema(description = "用户id")
 	private Long userId;

    /**
     * 退款编号
     */        
    @Schema(description = "退款编号")
 	private String refundNo;

    /**
     * 三方交易号
     */        
    @Schema(description = "三方交易号")
 	private String tradeNo;

    /**
     * 0待审核 1退款中 2退款完成 3退款拒绝
     */        
    @Schema(description = "0待审核 1退款中 2退款完成 3退款拒绝")
 	private Integer status;


}
