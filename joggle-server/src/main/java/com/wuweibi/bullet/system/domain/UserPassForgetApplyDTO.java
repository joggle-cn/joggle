package com.wuweibi.bullet.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 忘记密码申请dto
 * @author marker
 */
@Data
public class UserPassForgetApplyDTO {

    @ApiModelProperty("网站的URL地址")
    @NotBlank(message = "网站的URL地址不能空")
    private String siteUrl;

    @ApiModelProperty("已注册的邮箱账号地址")
    @NotBlank(message = "邮箱不能为空")
    private String email;


}
