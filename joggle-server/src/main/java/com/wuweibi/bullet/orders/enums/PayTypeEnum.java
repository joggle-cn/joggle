package com.wuweibi.bullet.orders.enums;

import com.wuweibi.bullet.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * 支付方式  1余额 2 支付宝
 *
 * @author marker
 */
@AllArgsConstructor
@Getter
public enum PayTypeEnum {

    BALANCE(1, "余额"),
    ALIPAY(2, "支付宝"),

    ;


    private int type;
    private String name;


    /**
     * 枚举数据map化处理，为便于获取枚举数据.
     */
    private static final Map<Integer, String> MAP = EnumUtil
            .toMap(PayTypeEnum.class, PayTypeEnum::getType, PayTypeEnum::getName);


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
