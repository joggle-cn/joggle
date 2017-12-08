package com.wuweibi.bullet.domain;

import com.faceinner.alias.Doc;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


/**
 * 心愿墙
 * @author marker
 * @version 1.0
 */
@Document(collection=Doc.Wishwall)
public class Wishwall implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	/** 名称 */
	private String name;
	/** 时间 */
	private Date date;
	
	/** 详细地址 */
	private String address;
	
	@DBRef(lazy=true)
	private Province province;
	
	@DBRef(lazy=true)
	private City city;
	
	@DBRef(lazy=true)
	private Country country;
	
	private String style;
	
	/** 背景图 */
	private String background;
	
	/** 访问量 */
	private long visited;
	
	
	/** 描述信息 */
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public long getVisited() {
		return visited;
	}

	public void setVisited(long visited) {
		this.visited = visited;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

 
	
	
	
}
