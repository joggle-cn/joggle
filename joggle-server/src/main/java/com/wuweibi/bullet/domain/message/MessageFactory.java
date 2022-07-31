package com.wuweibi.bullet.domain.message;


import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.alias.StateCode;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Iterator;
import java.util.List;


/**
 * API 消息工厂
 * （未来可加入缓存机制，将消息缓存至工厂缓存池，实现快速访问无需创建）
 * 
 * @author marker
 * @version 1.0
 */
public class MessageFactory {
 
	
	
	
	
	/**
	 * 获取消息
	 * @param msgId
	 * @return
	 */
	public static MessageResult get(int msgId){
		return new MessageResult(msgId);
	}

	public static MessageResult get(Object entity) { 
		return new MessageResult(entity);
	}
	
	
	/**
	 * 获取错误消息对象
	 * @param message 错误消息内容
	 * @return
	 */
	public static MessageResult getErrorMessage(String message){
		return new MessageResult(false, null, message);
	}

	/**
	 * 获取错误消息对象
	 * @param e 错误消息内容
	 * @return
	 */
	public static MessageResult getExceptionMessage(Exception e) {
		return new MessageResult(false, e.getMessage());
	}
	
	
	
	/**
	 * 用户未登录错误
	 * @return
	 */
	public static MessageResult getUserNotLoginError() {
		return new MessageResult(StateCode.UserNotLoginError);
	}

	
	



	/**
	 * 获取成功消息
	 * @return
	 */
	public static MessageResult getOperationSuccess() { 
		return get(StateCode.OperationSuccess);
	}



	/**
	 * 获取转换后的SpringMVC错误
	 * @param errors SpringMVC错误
	 * @return
     */
	public static MessageResult getErrorMessage(Errors errors) {
        List<FieldError> errorList = errors.getFieldErrors();
        Iterator<FieldError> it = errorList.iterator();
        FormFieldMessage[] errorCode = new FormFieldMessage[errorList.size()];
        int i = 0;
        while(it.hasNext()){
            FieldError oe = it.next();
            String field = oe.getField();
            errorCode[i++] = new FormFieldMessage(field, Integer.parseInt(oe.getCode()));
        }
		return new MessageResult(State.FormFieldError, errorCode);
	}


    /**
     * 获取表单错误
     * (针对一个表单错误)
     * @param ffm 错误消息
     * @return
     */
    public static MessageResult getForm(FormFieldMessage ffm) {
        return new MessageResult(State.FormFieldError, new Object[]{ffm});
    }
}
