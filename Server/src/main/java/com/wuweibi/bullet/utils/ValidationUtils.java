package com.wuweibi.bullet.utils;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;


/**
 * 验证工具
 * @author marker
 * @version 1.0
 */
public class ValidationUtils {

	
	
	/**
	 * 
	 * @param errors
	 * @param field
	 * @param stateCode
	 */
	public static void rejectIfEmpty(Errors errors, String field,
			int stateCode) {
		 rejectIfEmpty(errors, field, String.valueOf(stateCode), null, null);
	}
	
	
	
	
	/**
	 * Reject the given field with the given error code, error arguments
	 * and default message if the value is empty.
	 * <p>An 'empty' value in this context means either {@code null} or
	 * the empty string "".
	 * <p>The object whose field is being validated does not need to be passed
	 * in because the {@link Errors} instance can resolve field values by itself
	 * (it will usually hold an internal reference to the target object).
	 * @param errors the {@code Errors} instance to register errors on
	 * @param field the field name to check
	 * @param errorCode the error code, interpretable as message key
	 * @param errorArgs the error arguments, for argument binding via MessageFormat
	 * (can be {@code null})
	 * @param defaultMessage fallback default message
	 */
	public static void rejectIfEmpty(
			Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage) {

		Assert.notNull(errors, "Errors object must not be null");
		Object value = errors.getFieldValue(field);
		if (value == null || !StringUtils.hasLength(value.toString())) {
			errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
		}
	}




	public static void reject(Errors errs, String field, int stateCode) {
		errs.rejectValue(field, String.valueOf(stateCode));
	}

}
