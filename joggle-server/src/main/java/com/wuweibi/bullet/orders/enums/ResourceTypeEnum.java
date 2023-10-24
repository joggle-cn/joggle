package com.wuweibi.bullet.orders.enums;

import com.wuweibi.bullet.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * 资源类型 1端口 2域名 3流量 4 充值
 * @author marker
 */
@AllArgsConstructor
@Getter
public enum ResourceTypeEnum {

    DOMAIN(1, "端口"),
    PORT(2, "域名"),
    FLOW(3, "流量"),
    CHARGE(4, "充值"),
    PACKAGES(5, "套餐"),

    ;


    private int type;
    private String name;


    /**
     * 枚举数据map化处理，为便于获取枚举数据.
     */
    private static final Map<Integer, String> MAP = EnumUtil
            .toMap(ResourceTypeEnum.class, ResourceTypeEnum::getType, ResourceTypeEnum::getName);


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
