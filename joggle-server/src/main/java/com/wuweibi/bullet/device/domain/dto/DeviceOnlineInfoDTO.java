package com.wuweibi.bullet.device.domain.dto;


import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DeviceOnlineInfoDTO {

    /**
     * 设备编码
     */
    private String deviceNo;

    /**
     * 公网IP地址
     */
    @Schema(description = "公网IP地址")
    private String publicIp;

    private String intranetIp;

    private String clientVersion;

    private String macAddr;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "cpu架构")
    private String arch;

    @Schema(description = "通道id")
    private Integer serverTunnelId;

}
