package com.wuweibi.bullet.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NoticeSwitchParam {


    @ApiModelProperty("系统通知开关状态 1打开 0关闭")
    private Integer status;



}
