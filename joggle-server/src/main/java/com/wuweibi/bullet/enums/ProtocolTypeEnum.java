package com.wuweibi.bullet.enums;


import com.wuweibi.bullet.utils.EnumUtil;
import lombok.Getter;

import java.util.Map;

/**
 * 协议类型
 * @author marker
 */
@Getter
public enum ProtocolTypeEnum {

    HTTP(1, "http"),
    TCP(2, "tcp"),
    HTTPS(3, "https"),
    HTTPS_HTTP(4, "https"),
    UDP(5, "udp"),

    ;
    private Integer type;
    private String protocol;

    ProtocolTypeEnum(Integer type, String protocol){
        this.type = type;
        this.protocol = protocol;
    }

    /**
     * 枚举数据map化处理，为便于获取枚举数据.
     */
    public static final Map<Integer, String> MAP = EnumUtil
            .toMap(ProtocolTypeEnum.class, ProtocolTypeEnum::getType, ProtocolTypeEnum::getProtocol);

    /**
     * 枚举数据array化处理，为便于获取枚举数据.
     */
    public static final Integer[] ARRAY = EnumUtil.toArray(ProtocolTypeEnum.class, ProtocolTypeEnum::getType);


    /**
     * 获取协议
     * @param type
     * @return
     */
    public static final String getProtocol(int type){
        return MAP.get(type);
    }



}
