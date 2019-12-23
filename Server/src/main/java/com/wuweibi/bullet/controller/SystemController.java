package com.wuweibi.bullet.controller;

import com.wuweibi.bullet.config.BulletConfig;
import com.wuweibi.bullet.controller.validator.UserValidator;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 系统管理
 *
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {

	@Autowired private UserService userService;


	@Autowired
    private BulletConfig bulletConfig;
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		dateFormat.setLenient(false);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
		
		binder.setValidator(new UserValidator()); //添加一个spring自带的validator
	}




    /**
     * 安装ngrokd服务
     * @return
     */
    @RequestMapping(value="/ngrokd/install", method=RequestMethod.POST)
    public MessageResult ngrokdInstall(HttpServletRequest request){


        String ngrokdHomePath = bulletConfig.getNgrokd();






        return MessageFactory.getOperationSuccess();
    }


    /**
     * 检查是否安装Ngrokd服务
     * @param request
     * @return
     */
    @RequestMapping(value="/ngrokd/check", method=RequestMethod.GET)
    public MessageResult ngrokdCheck(HttpServletRequest request){
        String ngrokdHomePath = bulletConfig.getNgrokd();

        String ngrokdPath = ngrokdHomePath + "/bin/ngrokd";

        File file = new File(ngrokdPath);

        if(file.exists()){
            return MessageFactory.getOperationSuccess();
        }
        return MessageFactory.getErrorMessage("服务未安装");
    }


	
}
