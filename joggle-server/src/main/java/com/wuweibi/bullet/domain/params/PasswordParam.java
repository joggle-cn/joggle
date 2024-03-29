package com.wuweibi.bullet.domain.params;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 *
 * 登录参数
 *
 * @author marker
 * @version 1.0
 */
@Data
public class PasswordParam {

	/** 新密码 */
	@Pattern(regexp = "[a-zA-Z0-9.]{6,16}", message = "密码规则6-16位数字母.数字.")
	@NotBlank(message = "新密码不能为空")
	private String newPassword;

	/** 旧密码 */
	@NotBlank(message = "旧密码不能为空")
	private String oldPassword;



}
