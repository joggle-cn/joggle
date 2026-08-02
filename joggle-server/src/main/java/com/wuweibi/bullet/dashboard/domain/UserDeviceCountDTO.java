package com.wuweibi.bullet.dashboard.domain;

import lombok.Data;

/**
 * 用户设备数量统计
 */
@Data
public class UserDeviceCountDTO {

    /**
     * 设备总数
     */
    private Integer deviceCount = 0;

    /**
     * 在线设备数量
     */
    private Integer onlineDeviceCount = 0;

}
