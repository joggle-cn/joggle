package com.wuweibi.bullet.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * 登录验证模块
 * 
 * @author marker
 * @version 1.0
 */
public class LoginParamValidator implements Validator{

 
	
	@Override
	public boolean supports(Class<?> clazz) { 
//		return LoginParam.class.equals(clazz);
		return true;
	}

	
	
	@Override
	public void validate(Object target, Errors err) {

//	    ValidationUtils.rejectIfEmpty(err, "pass", State.PleseInputContent);
	    
	     
//	    ValidationUtils.rejectIfEmpty(err, "age", ErrorCode.MustFillInput);  
//	    ValidationUtils.rejectIfEmpty(err, "agree", ErrorCode.MustFillInput);  
//	    ValidationUtils.rejectIfEmpty(err, "pass", ErrorCode.MustFillInput);  
//	     
//	    ValidationUtils.rejectIfEmpty(err, "sex", ErrorCode.MustFillInput);  
//	     
//	     
	     
	     
	     
	}

}
