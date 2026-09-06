package com.wuweibi.bullet.system.api_key.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_api_key")
public class UserApiKey {

    private Long id;

    private Long userId;

    private String apiKey;

    private String name;

    private Boolean enabled;

    private Date lastUsedAt;

    private Date createTime;

    private Date updateTime;
}
