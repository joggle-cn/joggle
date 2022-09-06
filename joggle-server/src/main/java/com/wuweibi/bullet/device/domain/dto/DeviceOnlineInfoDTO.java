package com.wuweibi.bullet.device.domain.dto;


import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("公网IP地址")
    private String publicIp;

    private String intranetIp;

    private String clientVersion;

    private String macAddr;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("cpu架构")
    private String arch;

    @ApiModelProperty("通道id")
    private Integer serverTunnelId;
}
