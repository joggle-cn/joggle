package com.wuweibi.bullet.device.domain.dto;


import lombok.Data;

@Data
public class DeviceOnlineInfoDTO {

    /**
     * 设备编码
     */
    private String deviceNo;

    /**
     * 公网IP地址
     */
    private String publicIp;
}
