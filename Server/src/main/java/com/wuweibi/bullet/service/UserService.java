package com.wuweibi.bullet.service;


import com.wuweibi.bullet.domain.User;
import com.wuweibi.bullet.domain.message.MessageResult;

/**
 * 用户接口
 *
 *
 */
public interface UserService {

	int login(String email, String pass);
	
	User find(String email);

	User findByOpenId(String openID);
	
	void save(User user);


	/**
	 * 申请修改密码接口
	 *
     * @param url
     * @param email 电子邮箱地址
     * @param ip
     * @return
     */
	MessageResult applyChangePass(String email, String url, String ip);


    /***
     * 修改密码
     * @param code
     * @param pass
     * @return
     */
    MessageResult changePass4Code(String code, String pass);

}
