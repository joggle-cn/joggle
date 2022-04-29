package com.wuweibi.bullet.device.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ServerTunnelVO {

    private Integer id;
    //通道名称
    private String name;
    //宽带 MB
    private Integer broadband;
    //线路通道地址
    private String serverAddr;
    //上线时间
    private Date createTime;




}
