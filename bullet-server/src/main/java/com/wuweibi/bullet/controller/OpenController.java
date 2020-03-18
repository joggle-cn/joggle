package com.wuweibi.bullet.controller;


import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.controller.validator.LoginParamValidator;
import com.wuweibi.bullet.controller.validator.RegisterValidator;
import com.wuweibi.bullet.domain.message.FormFieldMessage;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.oauth2.service.OauthUserService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.utils.HttpUtils;
import com.wuweibi.bullet.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 登录读物
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/api/open")
public class OpenController {

	@Autowired private OauthUserService oauthUserService;
	
	/** 消息通知 */
	@Autowired private UserService userService;

	/** 密码加密器 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		dateFormat.setLenient(false);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
		
		binder.setValidator(new LoginParamValidator()); //添加一个spring自带的validator
	}




	/**
	 * 忘记密码
	 * @return
	 */
	@RequestMapping(value="/forget", method=RequestMethod.POST)
	public MessageResult forget(@RequestParam String email,
								HttpServletRequest request){
		// 验证邮箱正确性
		if(email.indexOf("@") == -1 && !SpringUtils.emailFormat(email)){// 邮箱格式不正确
			FormFieldMessage ffm = new FormFieldMessage();
			ffm.setField("email");
			ffm.setStatus(State.RegEmailError);
			return MessageFactory.getForm(ffm);
		}
		String ip = HttpUtils.getRemoteHost(request);
		String url = HttpUtils.getRequestURL(request);
		return userService.applyChangePass(email, url, ip);
	}


	/**
	 * 注册账号
	 * @return
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Object register(@ModelAttribute User user, Errors errors){
		// 验证邮箱正确性
		new RegisterValidator().validate(user, errors);
		if(errors.hasErrors()){
			return Result.fail(errors);
		}

		// 验证是否存在
		User u = userService.getByEmail(user.getEmail());
		if(u != null){
			errors.rejectValue("email", String.valueOf(State.RegEmailExist));
			return Result.fail(errors);
		}
		// 用户名设置为邮箱
		user.setUsername(user.getEmail());
		user.setEnabled(true); // TODO 目前没有做邮箱激活，直接启用账号

		// 密码加密处理
		String password = user.getPassword();
		user.setPassword(passwordEncoder.encode(password));

		boolean status = userService.save(user);
		if(status){
			// 赋权用户端
			userService.newAuthRole(user.getId(), "Consumer");

			// TODO 注册成功后发送激活邮件


		}


		return Result.success();
	}
	

	 
	
}
