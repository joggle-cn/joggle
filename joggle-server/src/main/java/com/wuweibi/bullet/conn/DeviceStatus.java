package com.wuweibi.bullet.conn;

/**
 * 设备状态
 * @author marker
 */
public enum DeviceStatus {

    ONLINE(1, "在线"),
    OUTLINE(-1, "离线"),
    ;


    public final int status;
    public final String name;


    DeviceStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

}
