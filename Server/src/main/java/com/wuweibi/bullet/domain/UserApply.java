package com.wuweibi.bullet.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户修改密码申请
 * @author marker
 * @version 1.0
 */
public class UserApply implements Serializable{
	private static final long serialVersionUID = 3214570480578175256L;




	/** ID */
	private long id;
	
	/** 用户Id */
	private long userId;
	
	/** Email */
	private String email;
	
	/** 旧密码 */
	private String oldPass;
	
	/** 创建时间 */
	private Date createTime;
	
	/** IP地址 */
	private String ip;

	/** 修改码 */
	private String code;
	
	/** 状态 */
	private int status = 0;

	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	
	
}
