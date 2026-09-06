package com.wuweibi.bullet.res.domain;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserPackageFowVO {

    private Long userId;
    @Schema(description = "套餐剩余流量KB")
    private String flow;
    @Schema(description = "套餐总流量KB")
    private Long flowTotal;
    private String name;
    private String userEmail;
    private Integer resourcePackageId;

    @Schema(description = "资源包流量 kb")
    private Long resourcePackageFlow;
    private Integer level;
    private Date endTime;
}
