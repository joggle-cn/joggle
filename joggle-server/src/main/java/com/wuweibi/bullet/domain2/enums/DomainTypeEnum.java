package com.wuweibi.bullet.domain2.enums;

import com.wuweibi.bullet.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * 类型： 1 端口 2域名
 *
 * @author marker
 */
@AllArgsConstructor
@Getter
public enum DomainTypeEnum {

    PORT(1, "端口"),
    DOMAIN(2, "域名"),

    ;


    private int type;
    private String name;


    /**
     * 枚举数据map化处理，为便于获取枚举数据.
     */
    private static final Map<Integer, String> MAP = EnumUtil
            .toMap(DomainTypeEnum.class, DomainTypeEnum::getType, DomainTypeEnum::getName);


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
