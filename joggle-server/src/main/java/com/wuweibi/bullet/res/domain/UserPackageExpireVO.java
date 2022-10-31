package com.wuweibi.bullet.res.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserPackageExpireVO {

    private Long userId;
    private String name;
    private String userEmail;
    private Integer resourcePackageId;
    private Integer level;
    private Date endTime;
}
