package com.wuweibi.bullet.protocol.domain;

import lombok.Data;

@Data
public class PeerConfig {
    public static final String CLIENT="client_client_type";
    public static final String SERVER="client_server_type";

    /**
     * 设备映射ID
     */
    private Long mappingId;

    private String type;
    private String appName;
    private String name;
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

    // P2P/WSS aggregate bandwidth limit (Mbps); null or 0 means unlimited.
    private Integer bandwidth;

    // Transport strategy: p2p, wss, or auto.
    private String strategy;

    // Token used to authenticate with the WSS relay.
    private String wssToken;
}
