package com.wuweibi.bullet.system.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserAdminParam {


    @Schema(description = "用户名")
    private String username;

    @Schema(description = "启用状态 1正常 0禁用")
    private Integer enabled;

    @Schema(description = "套餐id")
    private Integer packageId;

}
