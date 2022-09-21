package com.wuweibi.bullet.orders.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 退款单(OrdersRefund)分页对象
 *
 * @author marker
 * @since 2022-09-19 10:31:16
 */
@SuppressWarnings("serial")
@Data
public class OrdersRefundVO {

            @ApiModelProperty("id")
  	private Long id;    

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
     * 退款金额
     */        
    @ApiModelProperty("退款金额")
 	private BigDecimal refundAmount;

    /**
     * 退款单价
     */        
    @ApiModelProperty("退款单价")
 	private BigDecimal priceAmount;

    /**
     * 退款数量
     */        
    @ApiModelProperty("退款数量")
 	private Long amount;

    /**
     * 创建时间
     */        
    @ApiModelProperty("创建时间")
 	private Date createTime;

    /**
     * 更新时间
     */        
    @ApiModelProperty("更新时间")
 	private Date updateTime;

    /**
     * 退款时间
     */        
    @ApiModelProperty("退款时间")
 	private Date refundTime;

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

    /**
     * 退款原因
     */        
    @ApiModelProperty("退款原因")
 	private String reason;

}
