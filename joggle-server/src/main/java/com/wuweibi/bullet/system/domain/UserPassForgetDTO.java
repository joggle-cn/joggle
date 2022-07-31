package com.wuweibi.bullet.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserPassForgetDTO  {

    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不能空")
    private String pass;

    @ApiModelProperty("修改密码的code")
    @NotBlank(message = "code不能空")
    private String code;

}
