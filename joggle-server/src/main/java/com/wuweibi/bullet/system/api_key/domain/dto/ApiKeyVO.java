package com.wuweibi.bullet.system.api_key.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ApiKeyVO {

    private Long id;

    private String apiKey;

    private String name;

    private Boolean enabled;

    private Date lastUsedAt;

    private Date createTime;
}
