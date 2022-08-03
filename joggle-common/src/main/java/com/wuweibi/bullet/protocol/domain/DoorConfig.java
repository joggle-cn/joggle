package com.wuweibi.bullet.protocol.domain;

import lombok.Data;

@Data
public class DoorConfig {
    private Long deviceId;

    private String localPath;
    private String serverPath;

    private Integer enable;

}
