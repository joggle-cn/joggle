
package com.wuweibi.bullet.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@ApiModel(value = "短信验证码参数")
public class SendSmsDTO {

    @ApiModelProperty(value = "国家区号")
    private String countryCode = "86";

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "短信类型 登录 LOGIN")
    private String type;

    /**
     * 短信参数
     */
    private Map<String, Object> param;



}
