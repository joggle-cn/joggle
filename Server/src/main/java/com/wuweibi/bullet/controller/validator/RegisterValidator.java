package com.wuweibi.bullet.controller.validator;

import cn.com.wuweiit.alias.State;
import cn.com.wuweiit.validation.ValidationUtils;
import com.faceinner.domain.User;
import com.faceinner.utils.Utils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegisterValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz); 
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target;
	    String account = user.getName();
		if(account == null){
		    ValidationUtils.rejectIfEmpty(errors, "email", State.PleseInputContent);
	    }else{
	    	if(account.indexOf("@") != -1){// email
	    		if(!Utils.emailFormat(user.getName())){// 邮箱格式正确  
	    			errors.rejectValue("email", String.valueOf(State.RegEmailError));
				}	
	    	} 
	    }
	    ValidationUtils.rejectIfEmpty(errors, "pass", State.PleseInputContent);
//	     
	}

}
