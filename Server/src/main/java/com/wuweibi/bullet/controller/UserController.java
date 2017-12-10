package com.wuweibi.bullet.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.wuweibi.bullet.alias.SessionAttr;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.controller.validator.RegisterValidator;
import com.wuweibi.bullet.controller.validator.UserValidator;
import com.wuweibi.bullet.domain.ResultMessage;
import com.wuweibi.bullet.domain.message.FormFieldMessage;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.utils.HttpUtils;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired private UserService userService;

	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		dateFormat.setLenient(false);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
		
		binder.setValidator(new UserValidator()); //添加一个spring自带的validator
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
			return MessageFactory.getErrorMessage(errors);
		}

        // 验证是否存在
        User u = userService.getByEmail(user.getEmail());
        if(u != null){
            errors.rejectValue("email", String.valueOf(State.RegEmailExist));
            return MessageFactory.getErrorMessage(errors);
        }

        userService.insert(user);
		return MessageFactory.getOperationSuccess();
	}

	
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping(value="/login/info", method=RequestMethod.GET) 
	public MessageResult loginInfo(
			HttpSession session){
		// 验证邮箱正确性
		User loginUser = (User)session.getAttribute(SessionAttr.LOGIN_USER);
		if(loginUser == null){
			return MessageFactory.getUserNotLoginError();
		}
		return MessageFactory.get(loginUser);
	}
	
	
	
	
	@RequestMapping(value="/internet",method=RequestMethod.POST) 
	public ResultMessage register(HttpServletRequest request, @RequestParam("from") String from){
		HttpSession session = request.getSession(false);
		if(session != null){
			User user = new User();
			String accessToken = (String) session.getAttribute("demo_access_token");
			String openID = (String) session.getAttribute("demo_openid"); 
			if("qq".equals(from)){
	            UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
	            UserInfoBean userInfoBean;
				try {
					userInfoBean = qzoneUserInfo.getUserInfo();
					if (userInfoBean.getRet() == 0) {
//						user.setName(userInfoBean.getNickname());

//						user.setOpenId(openID);
						return new ResultMessage(true, user);
		            }else{
		            	return new ResultMessage(false, "很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
		            }
				} catch (QQConnectException e) {
					e.printStackTrace();
				} 
			}
			 
		}else{
			new ResultMessage(false,"操作失败 session invalid");
		}
		 
		
		return new ResultMessage(false,"操作失败");
	}
	
	
	
	/**
	 * 注销操作
	 */
	@RequestMapping(value="/loginout", method=RequestMethod.GET) 
	public MessageResult loginout(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
		return MessageFactory.getOperationSuccess(); 
		 
	}



    /**
     * 修改密码
     */
    @RequestMapping(value="/changepass", method=RequestMethod.POST)
    public MessageResult changepass(@RequestParam String pass,
                                    @RequestParam String code,
                                HttpServletRequest request){
        // 根据code查询用户信息
        return userService.changePass4Code(code, pass);
    }
	
}
