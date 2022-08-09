package com.wuweibi.bullet.common.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 通用Long类型 ID body参数
 * @author marker
 */
@Data
public class IdDTO {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空 支持字符串接参数")
    private Integer id;


}
