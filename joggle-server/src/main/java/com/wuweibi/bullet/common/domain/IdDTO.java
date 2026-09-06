package com.wuweibi.bullet.common.domain;


import lombok.Data;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 通用Long类型 ID body参数
 * @author marker
 */
@Data
public class IdDTO {

    @Schema(description = "id")
    @NotNull(message = "id不能为空 支持字符串接参数")
    private Integer id;


}
