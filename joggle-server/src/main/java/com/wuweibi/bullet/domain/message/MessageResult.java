package com.wuweibi.bullet.domain.message;

import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.alias.StateCode;
import com.wuweibi.bullet.annotation.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * 消息对象
 * @author marker
 * @version 1.0
 * 
 * @update 2015-02-11 添加了code属性，描述错误码，绑定国际化资源
 */
public class MessageResult {

	/** 日志记录器 */
	private Logger logger = LoggerFactory.getLogger(getClass());


	/** 消息内容缓存区 */
	private static final Map<Integer, String> messages = new HashMap<Integer, String>();


	/** 状态 */
	protected int status;

	/** 消息内容 */
	protected String msg;

	/** 消息 */
	protected Object data;


	/**
	 * 消息
	 * @param  data
	 * */
	public MessageResult(Object data){
		this.status  = StateCode.OperationSuccess;
		this.data = data;
	}



	/**
	 * 针对返回多个错误码的接口设计
	 * @param errorcode
	 */
	public MessageResult(String[] errorcode){
		this.status = StateCode.OperationError;
		this.data = errorcode;
	}


	/**
	 * 针对返回多个错误码的接口设计
	 * @param status
	 */
	public MessageResult(int status){
		this.status = status;
	}








	/**
	 * 默认操作成功消息
	 */
	public MessageResult() {
		this.status = StateCode.OperationSuccess;
	}



	/**
	 * 通用操作成功与操作失败处理方法
	 * （当status=true时， 状态码为：0）
	 * （当status=false时，状态码为：100500 ）
	 *
	 * @param status 状态
	 * @param data 消息
	 */
	public MessageResult(boolean status, String data) {
		if(status){
			this.status = StateCode.OperationSuccess;
		}else{
			this.status = StateCode.OperationError;
		}
		this.data = data;
	}

	/**
	 * 通用操作成功与操作失败处理方法
	 *
	 * @param status 状态
	 * @param data 消息
	 */
	public MessageResult(int status, Object data) {
		this.status = status;
		this.data = data;
	}



	public MessageResult(boolean status, Object data) {
		if(status){
			this.status = State.OperationSuccess;
		}else{
			this.status = State.OperationFieldError;
		}
		this.data = data;
	}

	public MessageResult(boolean status, Object data, String msg) {
		if(status){
			this.status = State.OperationSuccess;
		}else{
			this.status = State.OperationFieldError;
		}
		this.data = data;
		this.msg = msg;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public String getMsg() {
		if(null == msg ){
			try {
				if(messages.size() == 0){
					@SuppressWarnings("static-access")
					Class<?> clzz = getClass().forName("com.wuweibi.bullet.alias.State");
					Field[] fields =  clzz.getDeclaredFields();
					for(Field field : fields){
						Text t = field.getAnnotation(Text.class);
						if(t != null){
							messages.put((Integer)field.get(Integer.class), t.value());
						}
					}
				}
				return messages.get(this.status);
			} catch (Exception e) {
				logger.error("get MessageResult Info Error status="  +this.status , e);
			}
		}
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public Object getData() {
		return data;
	}



	public void setData(Object data) {
		this.data = data;
	}



 
	
	
}
