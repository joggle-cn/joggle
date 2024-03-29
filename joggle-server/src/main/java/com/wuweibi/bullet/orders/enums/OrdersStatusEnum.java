package com.wuweibi.bullet.orders.enums;

import com.wuweibi.bullet.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * 订单状态 0待支付 1已支付 2 取消 3退款中 4已退款
 *
 * @author marker
 */
@AllArgsConstructor
@Getter
public enum OrdersStatusEnum {

    WAIT_PAY(0, "待支付"),
    PAYED(1, "已支付"),
    CANCEL(2, "取消"),
    REFUNDING(3, "退款中"),
    REFUNDED(4, "已退款"),

    ;


    private int status;
    private String name;


    /**
     * 枚举数据map化处理，为便于获取枚举数据.
     */
    private static final Map<Integer, String> MAP = EnumUtil
            .toMap(OrdersStatusEnum.class, OrdersStatusEnum::getStatus, OrdersStatusEnum::getName);


    /**
     * 转换为Name
     *
     * @param status
     * @return
     */
    public static String toName(Integer status) {
        return MAP.get(status);
    }
}
