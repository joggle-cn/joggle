package com.wuweibi.bullet.domain;

import com.faceinner.alias.Doc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


/**
 * 省份
 * @author marker
 * @version 1.0
 */
@Document(collection=Doc.Province)
public class Province  implements Serializable {
	private static final long serialVersionUID = -7362439534537382731L;
	
	
	// 省份ID
	@Id
	private Integer id;
	
	private Integer pid;
	
	// 省份名称
	private String name;
	
	
	public Province() {
		// TODO Auto-generated constructor stub
	}
 
	public Province(Integer id) {
		super();
		this.id = id;
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
