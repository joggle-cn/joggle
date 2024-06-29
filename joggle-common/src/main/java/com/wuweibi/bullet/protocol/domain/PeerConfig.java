package com.wuweibi.bullet.protocol.domain;

import lombok.Data;

@Data
public class PeerConfig {
    public static final String CLIENT="client_client_type";
    public static final String SERVER="client_server_type";

    private String type;
    private String appName;
    private Integer port;

    // 本地Host
    private String host;

    private Integer enable;

    private Integer mtu;

    // ("传输压缩 1启用 0禁用")
    private Integer compress;

    // 传输加密方式 none aes aes-128
    private String encryption;

    // 循环周期(单位：ms) 10 20 30 40
    private Integer interval;
}
