package com.wuweibi.bullet.domain;

import com.faceinner.alias.Doc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 国家
 * 
 * @author marker
 * @version 1.0
 */
@Document(collection = Doc.Country)
public class Country {

	@Id
	private Integer id;

	/** 名称 */
	private String name;

	public Country() {}
	
	public Country(Integer id) {
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

}
