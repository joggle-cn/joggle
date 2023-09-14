package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.alias.MessageCode;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.domain.params.PasswordParam;
import com.wuweibi.bullet.entity.UserForget;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.BaseException;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.mapper.UserForgetMapper;
import com.wuweibi.bullet.mapper.UserMapper;
import com.wuweibi.bullet.res.service.UserPackageService;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.domain.dto.UserAdminParam;
import com.wuweibi.bullet.system.domain.vo.UserDetailVO;
import com.wuweibi.bullet.system.domain.vo.UserListVO;
import com.wuweibi.bullet.system.entity.User;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserForgetMapper userForgetMapper;

    @Resource
    private MailService mailService;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Transactional
    public MessageResult applyChangePass(String email, String url, String ip) {
        // 验证账号是否存在
        User user = this.baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getEmail, email));
        if (user == null) {// 邮箱账号不存在
            return MessageFactory.get(State.RegEmailNotExist);
        }

        // 生成修改密码验证串,生命周期为30分钟.
        String code = UUID.randomUUID().toString();

        UserForget apply = new UserForget();
        apply.setUserId(user.getId());
        apply.setEmail(email);
        apply.setOldPass(user.getPassword());
        apply.setCreateTime(new Date());
        apply.setCode(code);
        apply.setIp(ip);
        apply.setStatus(0);

        // 更新以前Email相关Code的status
        userForgetMapper.updateEmailStatus(email);

        userForgetMapper.insert(apply);

        // 发送激活邮件
        Map<String, Object> params = new HashMap<>(2);

        String forgetUrl = url + "#/forget?code=" + apply.getCode();
        params.put("forgetUrl", forgetUrl);
        params.put("url", url);
        String subject = "忘记密码申请 - Joggle服务通知";
        mailService.send(email, subject, params, "forget_mail.ftl");

        // 发送密码修改链接到邮箱
        return MessageFactory.getOperationSuccess();
    }

    @Override
    public MessageResult changePass4Code(String code, String pass) {
        // 密码加密
        String newPassword = pass;
        String passwordEncode = passwordEncoder.encode(newPassword);

        // 查询code信息
        UserForget userApply = userForgetMapper.findByCode(code);
        if (userApply == null) {//
            return MessageFactory.get(State.CodeInvalid);
        }
        long userId = userApply.getUserId();// 用户Id

        // 直接修改密码
        userMapper.updatePass(userId, passwordEncode);
        userForgetMapper.updateStatus(code);

        return MessageFactory.getOperationSuccess();
    }

    @Override
    public User getByEmail(String email) {

        QueryWrapper ew = new QueryWrapper();
        ew.eq("email", email);

        // 验证是否存在
        return this.baseMapper.selectOne(ew);
    }

    @Override
    public int login(String email, String pass) {
        User user = getByEmail(email);
        if (user != null) {
            String userPass = user.getPassword();
            if (userPass != null && userPass.equals(pass)) {

                this.baseMapper.updateLoginTime(user.getId());

                return MessageCode.login_suc_user;
            }
            return MessageCode.login_err_pass;
        }
        return MessageCode.login_err_email;
    }

    @Override
    public boolean updateBalance(Long userId, BigDecimal payMoney) {
        return this.baseMapper.updateBalance(userId, payMoney);
    }

    @Override
    public void newAuthRole(Long userId, String roleCode) {
        if (StringUtil.isNotBlank(roleCode)) {
            this.baseMapper.saveNewAuthRole(userId, roleCode);
        }

    }

    @Resource
    private UserFlowService userFlowService;

    @Override
    @Transactional
    public R activate(String code, String inviteCode) {

        User user = this.baseMapper.getByActivateCode(code);
        if (user == null) {
            return R.fail(SystemErrorType.ACCOUNT_ACTIVATE_FAILD);
        }

        Long userId = user.getId();
        this.baseMapper.updateEnabled(userId);

        // 如果存在邀请码的人存在，赠送流量给邀请人
        User inviteUser = this.getByInviteCode(inviteCode);
        if (inviteUser != null) { // 赠送到用户的套餐流量中
            userPackageService.updateFLow(inviteUser.getId(), 1048576L);
        }
        return R.success();
    }

    @Resource
    private UserPackageService userPackageService;


    @Override
    public boolean updatePassword(Long userId, PasswordParam dto) {
        User user = this.baseMapper.selectById(userId);
        if (user == null) {
            log.debug("user is not found {}", userId);
            return false;
        }

        String oldPassword = user.getPassword();
        if (!passwordEncoder.matches(dto.getOldPassword(), oldPassword)) {
            throw new BaseException(SystemErrorType.OLDPASSWORD_ERROR);
        }
        this.baseMapper.updatePass(userId, passwordEncoder.encode(dto.getNewPassword()));
        return true;
    }

    @Override
    public User getByInviteCode(String inviteCode) {
        return this.baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getActivateCode, inviteCode));
    }

    @Override
    public boolean updateUserCertification(Long userId, Integer state) {
        return this.baseMapper.updateUserCertification(userId, state);
    }

    @Override
    public Page<UserListVO> getList(Page pageInfo, UserAdminParam params) {
        return this.baseMapper.selectUserList(pageInfo, params);
    }

    @Override
    public UserDetailVO getDetailById(Long userId) {
        return this.baseMapper.selectDetailById(userId);
    }

    @Override
    public boolean updateSystemNotice(Long userId, Integer status) {
        if (!ArrayUtils.contains(new int[]{1, 0}, status)) {
            return false;
        }
        return this.baseMapper.updateSystemNotice(userId, status);
    }


}
