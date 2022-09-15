package com.wuweibi.bullet.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "短信验证码参数")
public class SmsDTO {

    @ApiModelProperty(value = "国家区号")
    private String countryCode = "86";

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "短信类型 登录 LOGIN")
    private String type;

    @ApiModelProperty("行为验证检查captchaVerification")
    private String captchaVerification;

}
