package com.wuweibi.bullet.controller;

import com.wuweibi.bullet.controller.validator.UserValidator;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.service.WeixinAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/api/weixin")
public class WeixinController {

	@Autowired private UserService userService;

	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		dateFormat.setLenient(false);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
		
		binder.setValidator(new UserValidator()); //添加一个spring自带的validator
	}



	@Autowired
    private WeixinAccountService weixinAccountService;




	/**
     * 绑定账号
     * @return
     */
    @RequestMapping(value="/bind", method=RequestMethod.GET)
    public MessageResult bind(
            @RequestParam String code,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String encryptedData,
            @RequestParam String iv,
            HttpServletRequest request){


        // 验证账号信息是否存在
        User user = userService.getByEmail(email);
        if(user != null){
            if(user.getPass().equals(password)){
                // 验证通过请求绑定数据
                return weixinAccountService.bind(user, code, encryptedData, iv);
            }

        }
        return MessageFactory.getErrorMessage("绑定失败！");
    }



	
}
