package com.wuweibi.bullet.service.impl;

import com.wuweibi.bullet.annotation.ResponseMessage;
import com.wuweibi.bullet.domain.message.MessageFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 拦截Controller返回值
 * 
 * @author marker
 * @version 1.0
 */
@Order(1)
@ControllerAdvice
public class RestResponseBodyAdvice implements ResponseBodyAdvice<Object> {

 
 
	@Override
	public boolean supports(MethodParameter returnType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getReturnType().isAssignableFrom(Object.class);
	}
	
	
	
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		ResponseMessage rm = returnType.getMethod().getAnnotation(ResponseMessage.class);
		if(null != rm){ 
			if(body instanceof Integer){// 直接返回状态
				int status = (Integer)body;
				return MessageFactory.get(status);
			}else if(body instanceof Errors){// SpringMVC错误对象
				Errors errs = (Errors)body;
				return MessageFactory.getErrorMessage(errs);
			}  
		}
		return body;
	}
}
