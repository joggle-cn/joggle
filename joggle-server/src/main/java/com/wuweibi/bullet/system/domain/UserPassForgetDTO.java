package com.wuweibi.bullet.system.domain;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserPassForgetDTO  {

    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能空")
    private String pass;

    @Schema(description = "修改密码的code")
    @NotBlank(message = "code不能空")
    private String code;

}
