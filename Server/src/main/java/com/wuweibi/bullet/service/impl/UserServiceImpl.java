package com.wuweibi.bullet.service.impl;

import cn.com.wuweiit.alias.State;
import cn.com.wuweiit.message.MessageFactory;
import cn.com.wuweiit.message.MessageResult;
import com.faceinner.alias.MessageCode;
import com.faceinner.dao.OAuthDao;
import com.faceinner.dao.UserDao;
import com.faceinner.dao.impl.UserApplyDao;
import com.faceinner.domain.User;
import com.faceinner.domain.UserApply;
import com.faceinner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 
 * @author marker
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
	
	 
	@Autowired UserDao userDao;
	
	@Autowired OAuthDao oAuthDao;
    @Autowired
    UserApplyDao applyDao;
	

    @Autowired
    MailSender mailSender;
//	public int login(String email, String pass) {
//		User user = userRep.findFirst(email);
//		if(user != null){
//			String userPass = user.getPass();
//			if(userPass != null && userPass.equals(pass)){
//				return MessageCode.login_suc_user;
//			}
//			return MessageCode.login_err_pass;
//		}
//		return MessageCode.login_err_email;
//	}




//	public User find(String email) {
//		return userRep.findFirst(email);
//	}




//	@Override
//	public User findByOpenId(String openID) {
//		return userRep.findByOpenId(openID);
//	}


	// 事务支持
	@Transactional
	public void save(User user) {
		userDao.save(user);
//		oAuthDao.saveUser(user.getId());
	}


	/**
	 * 修改密码
	 * @param email 电子邮箱地址
	 * @param ip 电子邮箱地址
	 * @param url
     * @return
     */
	@Override
	public MessageResult applyChangePass(String email, String url, String ip) {
        // 验证是否存在
        User user = this.find(email);
        if(user == null){// 邮箱账号不存在
            return MessageFactory.get(State.RegEmailNotExist);
        }

		// 生成修改密码验证串,生命周期为30分钟.
		String code = UUID.randomUUID().toString();

        UserApply apply = new UserApply();
        apply.setUserId(user.getId());
        apply.setEmail(email);
        apply.setOldPass(user.getPass());
        apply.setCreateTime(new Date());
        apply.setCode(code);
        apply.setIp(ip);


        // 更新以前Email相关Code的status
        applyDao.updateEmailStatus(email);


        applyDao.save(apply);


        // 判断是否手机




        // 发送激活邮件
        Map<String,Object> params = new HashMap<>(1);

        if(url.indexOf("http://faceinner.com") != -1){
            url = "https://faceinner.com";
        }

        String forgetUrl = url +"/mobi/forget.html?code="+apply.getCode();
        params.put("url", forgetUrl);

        mailSender.send(email, params, "forget_mail.ftl");


		// 发送密码修改链接到邮箱

		return MessageFactory.getOperationSuccess();
	}


    /**
     * 修改密码
     * @param code
     * @param pass
     * @return
     */
    @Override
    public MessageResult changePass4Code(String code, String pass) {
        // 查询code信息
        UserApply userApply = applyDao.findByCode(code);
        if(userApply == null){//
            return MessageFactory.get(State.CodeInvalid);
        }
        long userId = userApply.getUserId();// 用户Id
        // 直接修改密码
        userDao.updatePass(userId, pass);

        applyDao.updateStatus(code);

        return MessageFactory.getOperationSuccess();
    }


    @Override
	public int login(String email, String pass) {
		User user = userDao.findByEmail(email);
		if(user != null){
			String userPass = user.getPass();
			if(userPass != null && userPass.equals(pass)){
				return MessageCode.login_suc_user;
			}
			return MessageCode.login_err_pass;
		}
		return MessageCode.login_err_email;
	}


	@Override
	public User find(String email) {
		return userDao.findByEmail(email);
	}


	@Override
	public User findByOpenId(String openID) {
		// TODO Auto-generated method stub
		return null;
	}




	
	
	
}
