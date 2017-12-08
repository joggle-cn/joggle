package com.wuweibi.bullet.domain;

import com.faceinner.alias.Doc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


/**
 * 
 * @author marker
 * @version 1.0
 */
@Document(collection=Doc.City)
public class City implements Serializable {
	private static final long serialVersionUID = 9137928422962560757L;
	
	// 城市编码
	@Id
	private Integer id;
	// 省份编码
	private Integer pid;
	// 城市名称
	private String name;
	
	
	
	
	 
	public City(Integer id) {
		super();
		this.id = id;
	}
	public City() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	

	
	
}
