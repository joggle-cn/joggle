package com.wuweibi.bullet.orders.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态 0待支付 1已支付 2 取消 3退款中 4已退款
 * @author marker
 */
@AllArgsConstructor
@Getter
public enum OrdersStatusEnum {

    WAIT_PAY(0,"待支付"),
    PAYED(1,"已支付"),
    CANCEL(2,"取消"),
    REFUNDING(3,"退款中"),
    REFUNDED(4,"已退款"),

    ;


    private int status;
    private String name;
}
