package com.wuweibi.bullet.system.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class NoticeSwitchParam {


    @Schema(description = "系统通知开关状态 1打开 0关闭")
    private Integer status;



}
