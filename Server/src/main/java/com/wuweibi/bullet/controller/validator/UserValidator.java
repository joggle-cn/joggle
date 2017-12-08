package com.wuweibi.bullet.controller.validator;

import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.utils.Utils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override

	public void validate(Object target, Errors errors) {
		User user = (User)target;
	    String account = user.getNickname();
		if(account == null){
		    ValidationUtils.rejectIfEmpty(errors, "name", String.valueOf(State.PleseInputContent));
	    }else{
	    	if(account.indexOf("@") != -1){// email
	    		if(!Utils.emailFormat(user.getNickname())){// 邮箱格式正确
	    			errors.rejectValue("email", String.valueOf(State.RegEmailError));
				}	
	    	} 
	    }
	    ValidationUtils.rejectIfEmpty(errors, "pass", String.valueOf(State.PleseInputContent));
	    ValidationUtils.rejectIfEmpty(errors, "agree", String.valueOf(State.PleseInputContent));
	    ValidationUtils.rejectIfEmpty(errors, "pass", String.valueOf(State.PleseInputContent));
	    ValidationUtils.rejectIfEmpty(errors, "sex", String.valueOf(State.PleseInputContent));
//	     
	}

}
