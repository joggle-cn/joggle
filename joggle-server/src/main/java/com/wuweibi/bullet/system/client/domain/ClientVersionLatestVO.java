package com.wuweibi.bullet.system.client.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ClientVersionLatestVO {

    private String version;
    private String title;
    private String description;
    private String downloadUrl;
    private String checksum;
    private String os;
    private String arch;
    private Date createTime;
}
