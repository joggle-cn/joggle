package com.wuweibi.bullet.domain2.enums;

import com.wuweibi.bullet.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * 状态：1已售、0释放、-1 禁售
 *
 * @author marker
 */
@AllArgsConstructor
@Getter
public enum DomainStatusEnum {

    SALE(1, "已售"),
    BUY(0, "可售"),
    NO(-1, "禁售"),

    ;


    private int status;
    private String name;


    /**
     * 枚举数据map化处理，为便于获取枚举数据.
     */
    private static final Map<Integer, String> MAP = EnumUtil
            .toMap(DomainStatusEnum.class, DomainStatusEnum::getStatus, DomainStatusEnum::getName);


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
