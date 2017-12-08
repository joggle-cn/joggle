package com.wuweibi.bullet.service;

import com.faceinner.domain.City;
import com.faceinner.domain.Country;
import com.faceinner.domain.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public interface AddressService {

	Logger logger = LoggerFactory.getLogger(AddressService.class);
	
	/**
	 * 查询所有国家
	 * @return
	 */
	List<Country> getAllCountry();

	
	/**
	 * 查询省份
	 * @param cid
	 * @return
	 */
	List<Province> getProvinces(Serializable cid);
	


	/**
	 * 
	 * @param pid
	 * @return
	 */
	List<City> getCitys(Serializable pid);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	String getProvinceByCountryId(Serializable id);
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	String getCityByProvinceId(Serializable id);




}
