package com.wuweibi.bullet.dao.impl;

import com.faceinner.dao.AliasDao;
import com.faceinner.dao.UserDao;
import com.faceinner.domain.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static cn.com.wuweiit.builder.MapBuilder.newMap;


@Repository(AliasDao.USER)
public class UserDaoImpl implements UserDao{

	@Autowired
	private SqlSessionTemplate template;
	
 
	public void save(User entity) {
		template.insert("mapper.user.insert", entity);
	}

	
	public User findByEmail(String email) { 
		return template.selectOne("mapper.user.findByEmail", email); 
	}


	/**
	 * 修改密码
	 * @param userId 用户Id
	 * @param pass 密码
     */
	public int updatePass(long userId, String pass) {
		return template.update("mapper.user.updatePass", newMap(2)
				.setParam("userId", userId)
				.setParam("pass", pass)
				.build());
	}


    /**
     * 批量查询用户记录
     * @param userIdList id集合
     * @return
     */
    @Override
    public Map<Long, User> findByUserList(List<Long> userIdList) {
        Map<Long, User> map = new HashMap<>();
        List<User> list = template.selectList("mapper.user.findByUserList", userIdList);
        Iterator<User> it = list.iterator();
        while(it.hasNext()){
            User user = it.next();
            map.put(user.getId(), user);
        }
        return map;
    }


}
