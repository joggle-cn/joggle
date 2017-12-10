package com.wuweibi.bullet.domain.dto;/**
 * Created by marker on 2017/12/10.
 */

import com.wuweibi.bullet.entity.Device;

/**
 * @author marker
 * @create 2017-12-10 下午1:32
 **/
public class DeviceDto extends Device {

    private int status;

    public DeviceDto(Device device) {
        super();
        this.setName(device.getName());
        this.setCreateTime(device.getCreateTime());
        this.setId(device.getId());
        this.setDeviceId(device.getDeviceId());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
