package com.wuweibi.bullet.system.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class ClientVersionParam {


    @Schema(description = "标题")
    private String title;

    @Schema(description = "启用状态 1正常 0禁用")
    private Integer status;

}
