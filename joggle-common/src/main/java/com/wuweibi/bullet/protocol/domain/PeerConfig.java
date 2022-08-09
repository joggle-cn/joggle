package com.wuweibi.bullet.protocol.domain;

import lombok.Data;

@Data
public class PeerConfig {
    public static final String CLIENT="client_client_type";
    public static final String SERVER="client_server_type";

    private String type;
    private String appName;
    private Integer port;
    private Integer enable;
}
