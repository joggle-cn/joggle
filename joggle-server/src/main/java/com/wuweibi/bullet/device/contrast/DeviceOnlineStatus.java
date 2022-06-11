package com.wuweibi.bullet.device.contrast;

/**
 * 设备在线状态
 * @author marker
 */
public enum DeviceOnlineStatus {

    ONLINE(1, "在线"),
    OUTLINE(-1, "离线"),
    ;


    public final int status;
    public final String name;


    DeviceOnlineStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

}
