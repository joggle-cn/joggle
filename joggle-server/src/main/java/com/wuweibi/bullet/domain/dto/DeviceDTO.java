package com.wuweibi.bullet.domain.dto;/**
 * Created by marker on 2017/12/10.
 */

import com.wuweibi.bullet.device.entity.Device;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @author marker
 * @create 2017-12-10 下午1:32
 **/
@Data
public class DeviceDTO extends Device {

    private int status;

    private String intranetIp;
    private String deviceId;

    @ApiModelProperty("os操作系统")
    private String os;
    @ApiModelProperty("arch架构")
    private String arch;

    /**
     * 状态更新时间
     */
    private Date onlineTime;

    @ApiModelProperty("延迟(毫秒)")
    private Long latencyMs;

    public DeviceDTO(   ) {

    }
    public DeviceDTO(Device device) {
        super();
        this.setName(device.getName());
        this.setCreateTime(device.getCreateTime());
        this.setId(device.getId());
        this.setDeviceId(device.getDeviceNo());

    }

}
