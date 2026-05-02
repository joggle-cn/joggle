package com.wuweibi.bullet.res.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserPackageFowVO {

    private Long userId;
    @ApiModelProperty("套餐剩余流量KB")
    private String flow;
    @ApiModelProperty("套餐总流量KB")
    private Long flowTotal;
    private String name;
    private String userEmail;
    private Integer resourcePackageId;

    @ApiModelProperty("资源包流量 kb")
    private Long resourcePackageFlow;
    private Integer level;
    private Date endTime;
}
