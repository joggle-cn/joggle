package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.alias.MessageCode;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.entity.UserForget;
import com.wuweibi.bullet.mapper.UserForgetMapper;
import com.wuweibi.bullet.mapper.UserMapper;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private UserForgetMapper userForgetMapper;

    @Autowired
    private MailService mailService;





    public MessageResult applyChangePass(String email, String url, String ip) {

        QueryWrapper ew = new QueryWrapper();
        ew.eq("email", email);



        // 验证是否存在
        User user = this.baseMapper.selectOne(ew);
        if(user == null){// 邮箱账号不存在
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
        Map<String,Object> params = new HashMap<>(1);

        if(url.indexOf("http://faceinner.com") != -1){
            url = "https://faceinner.com";
        }

        String forgetUrl = url +"#/forget?code="+apply.getCode();
        params.put("url", forgetUrl);

        mailService.send(email, params, "forget_mail.ftl");


        // 发送密码修改链接到邮箱

        return MessageFactory.getOperationSuccess();
    }

    @Override
    public MessageResult changePass4Code(String code, String pass) {
        // 查询code信息
        UserForget userApply = userForgetMapper.findByCode(code);
        if(userApply == null){//
            return MessageFactory.get(State.CodeInvalid);
        }
        long userId = userApply.getUserId();// 用户Id
        // 直接修改密码
        userMapper.updatePass(userId, pass);

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
        if(user != null){
            String userPass = user.getPassword();
            if(userPass != null && userPass.equals(pass)){

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
        if(StringUtil.isNotBlank(roleCode)){
            this.baseMapper.saveNewAuthRole(userId, roleCode);
        }

    }


}
