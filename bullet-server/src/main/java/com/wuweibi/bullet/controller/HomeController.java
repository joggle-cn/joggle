package com.wuweibi.bullet.controller;

import com.wuweibi.bullet.domain.vo.CountVO;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.oauth2.manager.ResourceManager;
import com.wuweibi.bullet.service.CountService;
import com.wuweibi.bullet.utils.ConfigUtils;
import com.wuweibi.bullet.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 主页控制器
 * @author marker
 * @version 1.0
 */
@Controller
public class HomeController {

	@RequestMapping("")
	public String welcome(HttpServletRequest request){
		return "redirect:index.html";
	}
	/**
	 * 首页
	 * 重写首页，可自动判断访问的域名，识别接口ht。
	 * @return
	 */
	@RequestMapping("/index.html")
	public String home(HttpServletRequest request){
		/* 基础信息收集 */
        request.setAttribute("url", HttpUtils.getRequestURL(request));// 网址路径
        request.setAttribute("lang", HttpUtils.getLanguage(request));// 网址路径

		return "index";
	}


	/**
	 * 接口资源管理器
	 */
	@Resource
	private ResourceManager resourceManager;

    /**
     * 初始化接口
     * @return
     */
	@GetMapping("/api/open/init")
	@ResponseBody
	public Result init(){
		String domain = ConfigUtils.getBulletDomain();
        Map map = new HashMap(3);
        map.put("domain", domain);
        map.put("clientVersion", "v1.2.3");
        map.put("serverVersion", "v1.2.0");
        map.put("dockerClientVersion", "0.0.6");
		resourceManager.loadResource();
		return Result.success(map);
	}


	@Resource
	private CountService countService;

	/**
	 * 统计数据接口
	 * @return
	 */
	@GetMapping("/api/open/statistics")
	@ResponseBody
	public Result statistics(){
		CountVO countVO = countService.getCountInfo();
		return Result.success(countVO);
	}

}
