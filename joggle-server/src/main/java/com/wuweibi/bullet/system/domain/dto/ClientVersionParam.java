package com.wuweibi.bullet.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClientVersionParam {


    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("启用状态 1正常 0禁用")
    private Integer status;

}
