package com.wuweibi.bullet.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserCertAdminParam {


    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("姓名")
    private String realName;



}
