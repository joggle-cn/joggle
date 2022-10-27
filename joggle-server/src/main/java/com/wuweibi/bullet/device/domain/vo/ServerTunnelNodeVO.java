package com.wuweibi.bullet.device.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ServerTunnelNodeVO {

    @ApiModelProperty("通道id")
    private Integer id;
    //通道名称
    @ApiModelProperty("通道名称")
    private String name;

    @ApiModelProperty("通道地区")
    private String area;

    @ApiModelProperty("宽带mbps")
    private Integer broadband;

    @ApiModelProperty("线路通道地址")
    private String serverAddr;

    @ApiModelProperty("在线时长")
    private String onlineTime;

    @ApiModelProperty("入网流量GB")
    private Long flowIn;

    @ApiModelProperty("出网流量GB")
    private Long flowOut;

    @ApiModelProperty("在线设备数量")
    private Long deviceNum;

    @ApiModelProperty("在线通道数量")
    private Long tunnelNum;

    @ApiModelProperty("通道状态")
    private Integer status;

    @ApiModelProperty("通道上线时间")
    private Date serverUpTime;


}
