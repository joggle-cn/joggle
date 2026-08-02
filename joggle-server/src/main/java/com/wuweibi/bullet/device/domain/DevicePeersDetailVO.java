package com.wuweibi.bullet.device.domain;

import com.wuweibi.bullet.device.entity.DevicePeers;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 端到端映射详情对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DevicePeersDetailVO extends DevicePeers {

    @ApiModelProperty("客户端设备编号")
    private String clientDeviceNo;

    @ApiModelProperty("客户端设备名称")
    private String clientDeviceName;

    @ApiModelProperty("服务端设备编号")
    private String serverDeviceNo;

    @ApiModelProperty("服务端设备名称")
    private String serverDeviceName;
}
