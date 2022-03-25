package com.wuweibi.bullet.service;
/**
 * Created by marker on 2019/6/5.
 */


import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.entity.WeixinAccount;

/**
 * @author marker
 * @create 2019-06-05 17:53
 **/
public interface WeixinAccountService extends IService<WeixinAccount> {


    /**
     * 绑定用户
     * @param user
     * @param s
     * @param encryptedData
     * @param code
     */
    MessageResult bind(User user, String s, String encryptedData, String code);
}
