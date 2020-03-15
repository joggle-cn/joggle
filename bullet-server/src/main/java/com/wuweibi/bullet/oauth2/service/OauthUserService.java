package com.wuweibi.bullet.oauth2.service;

import com.wuweibi.bullet.domain.message.MessageResult;
import com.baomidou.mybatisplus.service.IService;
import com.wuweibi.bullet.oauth2.domain.OauthUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
public interface OauthUserService extends IService<OauthUser> {

    OauthUser getByUsername(String userName);


    /**
     * 根据Email获取
     * @param email
     * @return
     */
    OauthUser getByEmail(String email);


    /**
     * 登录校验
     * @param email email
     * @param pass 密码
     * @return
     */
    int login(String email, String pass);

    /**
     * 更新登录时间
     * @param id 用户ID
     */
    void updateLoginTime(Long id);
}
