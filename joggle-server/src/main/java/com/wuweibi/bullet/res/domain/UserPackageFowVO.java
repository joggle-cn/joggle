package com.wuweibi.bullet.res.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserPackageFowVO {

    private Long userId;
    private String flow;
    private String name;
    private String userEmail;
    private Integer resourcePackageId;

    @ApiModelProperty("资源包流量 kb")
    private Long resourcePackageFlow;
    private Integer level;
    private Date endTime;
}
