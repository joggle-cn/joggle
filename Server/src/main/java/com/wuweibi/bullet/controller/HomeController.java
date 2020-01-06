package com.wuweibi.bullet.controller;

import com.wuweibi.bullet.alias.SessionAttr;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.oauth2.manager.ResourceManager;
import com.wuweibi.bullet.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import java.util.HashMap;
import java.util.Map;


/**
 * 主页控制器
 * @author marker
 * @version 1.0
 */
@RestController
public class HomeController {

	
	@RequestMapping("/test")
	public String home(HttpServletRequest request, HttpSession session){
		/* 基础信息收集 */
        request.setAttribute("url", HttpUtils.getRequestURL(request));// 网址路径
        request.setAttribute("lang", HttpUtils.getLanguage(request));// 网址路径
        User user = (User) session.getAttribute(SessionAttr.LOGIN_USER);
        request.setAttribute("user", user); //
		return "index";
	}


	/**
	 * 接口资源管理器
	 */
	@Autowired
	ResourceManager resourceManager;

    /**
     * 初始化接口
     * @return
     */
	@GetMapping("/api/open/init")
	public Result init(){
        Map map = new HashMap(3);
        map.put("domain", "joggle.cn");
			resourceManager.loadResource();
		return Result.success(map);
	}

}
