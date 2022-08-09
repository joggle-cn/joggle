package com.wuweibi.bullet.device.contrast;

import com.wuweibi.bullet.utils.EnumUtil;
import lombok.Getter;

import java.util.Map;

/**
 * 任意门开启状态
 * @author marker
 */
@Getter
public enum DevicePeerStatusEnum {

    ENABLE(1, "启用"),
    DISABLE(0, "停用"),
    ;


    public final int status;
    public final String name;


    DevicePeerStatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }


    /**
     * 枚举数据map化处理，为便于获取枚举数据.
     */
    private static final Map<Integer, String> MAP = EnumUtil
            .toMap(DevicePeerStatusEnum.class, DevicePeerStatusEnum::getStatus, DevicePeerStatusEnum::getName);


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
