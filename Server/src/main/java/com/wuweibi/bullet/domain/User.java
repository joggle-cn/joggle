package com.wuweibi.bullet.domain;

import com.faceinner.alias.Doc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


/**
 * 用户
 * @author marker
 * @version 1.0
 */
@Document(collection = Doc.User)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	/** 用户名 */
	private String name;
	/** Email */
	private String email;
	/** 密码 */
	private String pass;
	/** 头像 */
	private String icon = "/resource/images/head.jpg";
	/** 年龄 */
	private int age = 0;
	/** 签名 */
	private String underwrite;
	/** 同意 */
	private boolean agree;
	/** 性别 */
	private int sex;
	
	/** 开发平台Id */
	private String openId;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getUnderwrite() {
		return underwrite;
	}
	public void setUnderwrite(String underwrite) {
		this.underwrite = underwrite;
	}
	public boolean isAgree() {
		return agree;
	}
	public void setAgree(boolean agree) {
		this.agree = agree;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	
	
}
