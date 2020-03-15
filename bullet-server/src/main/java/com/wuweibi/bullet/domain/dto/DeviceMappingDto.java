package com.wuweibi.bullet.domain.dto;/**
 * Created by marker on 2017/12/10.
 */

import com.wuweibi.bullet.entity.DeviceMapping;

/**
 * @author marker
 * @create 2017-12-10 下午12:53
 **/
public class DeviceMappingDto extends DeviceMapping {

    private String deviceCode;// 设备编码



    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
