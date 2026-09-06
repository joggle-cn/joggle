package com.wuweibi.bullet.system.domain;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户实名认证(UserCertification)分页对象
 *
 * @author marker
 * @since 2022-09-14 13:54:44
 */
@SuppressWarnings("serial")
@Data
public class UserCertificationDTO {

    /**
     * 类型1身份证
     */        
    @Schema(description = "类型 1身份证")
 	private Integer type;

    /**
     * 真实姓名
     */        
    @Schema(description = "真实姓名")
    @NotBlank(message = "姓名不能为空")
 	private String realName;

    /**
     * 身份证号码
     */        
    @Schema(description = "身份证号码")
    @NotBlank(message = "身份证号码不能为空")
 	private String idcard;

    @Schema(description = "国家区号")
    private String countryCode = "86";

    /**
     * 手机号码
     */        
    @Schema(description = "手机号码")
    @NotBlank(message = "手机号码不能为空")
 	private String phone;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
 	private String code;


}
