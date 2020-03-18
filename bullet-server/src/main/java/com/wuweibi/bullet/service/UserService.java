package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.entity.api.Result;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
public interface UserService extends IService<User> {


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


    /**
     * 根据Email获取
     * @param email
     * @return
     */
    User getByEmail(String email);


    /**
     * 登录校验
     * @param email email
     * @param pass 密码
     * @return
     */
    int login(String email, String pass);

    /**
     * 更新用户余额
     * @param userId 用户Id
     * @param payMoney 支付金额
     * @return
     */
    boolean updateBalance(Long userId, BigDecimal payMoney);


    /**
     * 给用户赋权角色
     * (不会验证权限是否存在的)
     * @param userId 用户Id
     * @param roleCode
     */
    void newAuthRole(Long userId, String roleCode);


    /**
     * 激活用户
     * @param code 激活码
     * @return
     */
    Result activate(String code);
}
