package com.wuweibi.bullet.system.domain;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 忘记密码申请dto
 * @author marker
 */
@Data
public class UserPassForgetApplyDTO {

    @Schema(description = "网站的URL地址")
    @NotBlank(message = "网站的URL地址不能空")
    private String siteUrl;

    @Schema(description = "已注册的邮箱账号地址")
    @NotBlank(message = "邮箱不能为空")
    private String email;


}
