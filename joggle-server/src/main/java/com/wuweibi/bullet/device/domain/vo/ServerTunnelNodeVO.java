package com.wuweibi.bullet.device.domain.vo;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class ServerTunnelNodeVO {

    @Schema(description = "通道id")
    private Integer id;
    //通道名称
    @Schema(description = "通道名称")
    private String name;

    @Schema(description = "通道地区")
    private String area;

    @Schema(description = "宽带mbps")
    private Integer broadband;

    @Schema(description = "线路通道地址")
    private String serverAddr;

    @Schema(description = "在线时长")
    private String onlineTime = "-";

    @Schema(description = "入网流量GB")
    private Long flowIn;

    @Schema(description = "出网流量GB")
    private Long flowOut;

    @Schema(description = "在线设备数量")
    private Long deviceNum;

    @Schema(description = "在线通道数量")
    private Long tunnelNum;

    @Schema(description = "通道状态 1在线 0不在线")
    private Integer status;

    @Schema(description = "通道上线时间")
    private Date serverUpTime;
    @Schema(description = "通道离线时间")
    private Date serverDownTime;


}
