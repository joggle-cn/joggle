package com.wuweibi.bullet.service.impl;

import com.faceinner.alias.CacheAlias;
import com.faceinner.domain.City;
import com.faceinner.domain.Country;
import com.faceinner.domain.Province;
import com.faceinner.repository.CityRepository;
import com.faceinner.repository.CountryRepository;
import com.faceinner.repository.ProvinceRepository;
import com.faceinner.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * @author marker
 * @version 1.0
 */
@Service
public class AddressServiceImpl implements AddressService {

	
	
	@Autowired private CountryRepository countryRep;
	
	@Autowired private ProvinceRepository provinceRep;
	
	@Autowired private CityRepository cityRep;
	
	
	
	@Cacheable(value = CacheAlias.country, key = "addressService.getAllCountry")
	public List<Country> getAllCountry() {
		logger.info("not cache! invoke getAllCountry() "); 
		return countryRep.findAll();
	}
	
	

	@Override
	public String getProvinceByCountryId(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCityByProvinceId(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Province> getProvinces(Serializable cid) {
		return provinceRep.find(cid);
	}



	@Override
	public List<City> getCitys(Serializable pid) {
		return cityRep.find(pid);
	}

}
