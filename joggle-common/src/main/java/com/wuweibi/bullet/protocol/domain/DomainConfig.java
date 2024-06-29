package com.wuweibi.bullet.protocol.domain;

import lombok.Data;

@Data
public class DomainConfig {
    private String deviceNo;

    private Long mapId;

    private Integer concurrentNum;

    private Integer bandwidth;

}
