package com.wuweibi.bullet.orders.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty("订单id")
 	private Long orderId;

    /**
     * 用户id
     */        
    @ApiModelProperty("用户id")
 	private Long userId;

    /**
     * 退款编号
     */        
    @ApiModelProperty("退款编号")
 	private String refundNo;

    /**
     * 三方交易号
     */        
    @ApiModelProperty("三方交易号")
 	private String tradeNo;

    /**
     * 0待审核 1退款中 2退款完成 3退款拒绝
     */        
    @ApiModelProperty("0待审核 1退款中 2退款完成 3退款拒绝")
 	private Integer status;


}
