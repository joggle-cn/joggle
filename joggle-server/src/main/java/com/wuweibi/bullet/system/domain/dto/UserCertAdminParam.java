package com.wuweibi.bullet.system.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserCertAdminParam {


    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "姓名")
    private String realName;



}
