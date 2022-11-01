package com.wuweibi.bullet.res.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserPackageFowVO {

    private Long userId;
    private String flow;
    private String userEmail;
    private Integer resourcePackageId;
    private Long resourcePackageFlow;
    private Integer level;
    private Date endTime;
}
