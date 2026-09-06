package com.wuweibi.bullet.system.domain;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "短信验证码参数")
public class SmsDTO {

    @Schema(description = "国家区号")
    private String countryCode = "86";

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "短信类型 登录 LOGIN")
    private String type;

    @Schema(description = "行为验证检查captchaVerification")
    private String captchaVerification;

}
