package com.wuweibi.bullet.oauth2.manager;

/**
 * 登录失败次数处理
 *
 * @author marker
 *
 */
public interface LoginCountService {


	/**
	 * 登录次数+1
	 * @param username
	 */
	void countPlus1(String username);

	/**
	 * 获取登录失败超过次数的等待时间
	 * @return
	 */
	int getFaildWaitTime();

	/**
	 * 获取可登录失败的总次数
	 * @return
	 */
	int getLoginTotalCount();

	/**
	 * 获取登录次数
	 * @param username
	 * @return
	 */
	int getCount(String username);

	/**
	 * 清除登录次数
	 * @param username
	 */
	void clean(String username);


	/**
	 * 获取剩余登录次数
	 * @param username 用户名
	 * @return
	 */
	 int getRemainLoginCount(String username);

	/**
	 * 剩余等待秒数
	 * @return
	 */
	int getWaitedTime(String username);


}
