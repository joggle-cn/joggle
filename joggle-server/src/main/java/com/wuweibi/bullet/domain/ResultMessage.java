package com.wuweibi.bullet.domain;


/**
 * 消息对象
 * @author marker
 * @version 1.0
 * 
 * @update 2015-02-11 添加了code属性，描述错误码，绑定国际化资源
 */
@Deprecated
public class ResultMessage {

	/** 状态 */
	private boolean status;
	
	/** 状态码 */
	private String code;
	
	/** 消息 */
	private Object data;
	
	
	/**
	 * 消息
	 * @param boolean status
	 * @param String message
	 * */
	public ResultMessage(boolean status, Object data){
		this.status = status;
		this.data = data;
	}
	
	 

	public ResultMessage(boolean status, String code, Object data) {
		super();
		this.status = status;
		this.code = code;
		this.data = data;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}



	public Object getData() {
		return data;
	}



	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
	
}
