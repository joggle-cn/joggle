package com.wuweibi.bullet.controller.validator;

import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.utils.SpringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class RegisterValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "password", String.valueOf(State.PleseInputContent));
		ValidationUtils.rejectIfEmpty(errors, "email", String.valueOf(State.PleseInputContent));
		ValidationUtils.rejectIfEmpty(errors, "nickname", String.valueOf(State.PleseInputContent));


		User user = (User)target;
	    String email = user.getEmail();

		if(!SpringUtils.emailFormat(email)){// 邮箱格式正确
			errors.rejectValue("email", String.valueOf(State.RegEmailError));
		}
//
	}

}
