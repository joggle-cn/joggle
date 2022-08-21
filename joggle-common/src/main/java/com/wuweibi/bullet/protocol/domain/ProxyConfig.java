package com.wuweibi.bullet.protocol.domain;

import lombok.Data;

@Data
public class ProxyConfig {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 代理Port
     */
    private Integer proxyPort;

    /**
     * 代理Host 默认0.0.0.0
     */
    private String proxyHost;
    /**
     * 代理类型 socks5 http
     */
    private String type;

    private Integer status;

}
