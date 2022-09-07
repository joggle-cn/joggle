package com.wuweibi.bullet.domain.dto;/**
 * Created by marker on 2017/12/10.
 */

import com.wuweibi.bullet.device.entity.Device;
import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @author marker
 * @create 2017-12-10 下午1:32
 **/
@Data
public class DeviceDto extends Device {

    private int status;

    private String intranetIp;
    private String deviceId;

    /**
     * 状态更新时间
     */
    private Date onlineTime;

    public DeviceDto(Device device) {
        super();
        this.setName(device.getName());
        this.setCreateTime(device.getCreateTime());
        this.setId(device.getId());
        this.setDeviceId(device.getDeviceNo());

    }

}
