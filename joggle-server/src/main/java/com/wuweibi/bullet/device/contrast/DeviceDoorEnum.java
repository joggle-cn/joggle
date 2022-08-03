package com.wuweibi.bullet.device.contrast;

/**
 * 任意门开启状态
 * @author marker
 */
public enum DeviceDoorEnum {

    ENABLE(1, "启用"),
    DISABLE(0, "停用"),
    ;


    public final int status;
    public final String name;


    DeviceDoorEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

}
