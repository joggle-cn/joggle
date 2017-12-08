package com.wuweibi.bullet.dao;

import com.faceinner.domain.User;

import java.util.List;
import java.util.Map;


/**
 * 用户
 * @author marker
 * @version 1.0
 */
public interface UserDao {
 
	public void save(User entity);

	public User findByEmail(String email);



	/**
	 * 修改密码
	 * @param userId 用户Id
	 * @param pass 密码
     */
    int updatePass(long userId, String pass);


    /**
     * 批量查询用户记录
     * @param userIdList id集合
     * @return
     */
    Map<Long,User> findByUserList(List<Long> userIdList);


}
