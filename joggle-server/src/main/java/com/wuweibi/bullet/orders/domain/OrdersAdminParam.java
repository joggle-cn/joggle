package com.wuweibi.bullet.orders.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrdersAdminParam {

    @ApiModelProperty("资源类型 1域名 2端口 3流量 4 充值")
    private Integer resourceType;

    @ApiModelProperty("支付方式 1余额 2支付宝")
    private Integer payType;

    @ApiModelProperty("状态  0待支付 1已支付 2 取消 3退款中 4已退款")
    private Integer status;
    @ApiModelProperty("订单号")
    private String orderNo;
}
