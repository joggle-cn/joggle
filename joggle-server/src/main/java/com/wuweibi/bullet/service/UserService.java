package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.domain.params.PasswordParam;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.system.domain.dto.UserAdminParam;
import com.wuweibi.bullet.system.domain.vo.UserDetailVO;
import com.wuweibi.bullet.system.domain.vo.UserListVO;
import com.wuweibi.bullet.system.entity.User;

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
     * @param code 修改码
     * @param pass 明文密码
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
     *
     * @param code 激活码
     * @param ic 邀请码
     * @return
     */
    R activate(String code, String ic);

    /**
     * 更新密码
     * @param userId
     * @param dto
     * @return
     */
    boolean updatePassword(Long userId, PasswordParam dto);

    /**
     * 根据邀请码查询用户
     */
    User getByInviteCode(String inviteCode);

    /**
     * 更新认证 状态
     * @param userId 用户id
     * @param state 状态
     * @return
     */
    boolean updateUserCertification(Long userId, Integer state);


    /**
     * 用户分页查询
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<UserListVO> getList(Page pageInfo, UserAdminParam params);

    UserDetailVO getDetailById(Long userId);


    /**
     * 更新系统通知状态
     * @param userId 用户id
     * @param status 状态 1打开 0关闭
     * @return
     */
    boolean updateSystemNotice(Long userId, Integer status);

    User getByUserId(Long userId);
}
