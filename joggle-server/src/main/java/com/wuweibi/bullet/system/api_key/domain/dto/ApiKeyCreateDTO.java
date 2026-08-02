package com.wuweibi.bullet.system.api_key.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApiKeyCreateDTO {

    @NotBlank(message = "名称不能为空")
    private String name;
}
