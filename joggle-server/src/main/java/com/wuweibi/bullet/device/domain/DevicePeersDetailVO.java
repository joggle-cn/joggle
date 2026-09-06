package com.wuweibi.bullet.device.domain;

import com.wuweibi.bullet.device.entity.DevicePeers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 端到端映射详情对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DevicePeersDetailVO extends DevicePeers {

    @Schema(description = "客户端设备编号")
    private String clientDeviceNo;

    @Schema(description = "客户端设备名称")
    private String clientDeviceName;

    @Schema(description = "服务端设备编号")
    private String serverDeviceNo;

    @Schema(description = "服务端设备名称")
    private String serverDeviceName;
}
