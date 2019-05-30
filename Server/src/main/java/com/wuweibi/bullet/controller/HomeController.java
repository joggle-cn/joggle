package com.wuweibi.bullet.controller;

import com.wuweibi.bullet.alias.SessionAttr;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 主页控制器
 * @author marker
 * @version 1.0
 */
@Controller
public class HomeController {

	
	@RequestMapping("/")
	public String home(HttpServletRequest request, HttpSession session){
		/* 基础信息收集 */
        request.setAttribute("url", HttpUtils.getRequestURL(request));// 网址路径
        request.setAttribute("lang", HttpUtils.getLanguage(request));// 网址路径
        User user = (User) session.getAttribute(SessionAttr.LOGIN_USER);
        request.setAttribute("user", user); //
		return "index";
	}

}
