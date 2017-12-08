package com.wuweibi.bullet.dao.impl;

import com.faceinner.dao.AliasDao;
import com.faceinner.dao.OAuthDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(AliasDao.OAUTH)
public class OAuthDaoImpl implements OAuthDao {

	@Autowired
	private SqlSessionTemplate template;
	
 
	
	@Override
	public void saveUser(long userId) {
		
		template.insert("mapper.oauth.insertUser", userId); 
		
	}

}
