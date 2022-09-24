package com.wuweibi.bullet.controller;

import com.wuweibi.bullet.client.service.ClientVersionService;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.domain.vo.CountVO;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.oauth2.manager.ResourceManager;
import com.wuweibi.bullet.service.CountService;
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
 *
 *
 * @author marker
 * @version 1.0
 */
@Controller
public class HomeController {

	/**
	 * 接口资源管理器
	 */
	@Resource
	private ResourceManager resourceManager;


	/**
	 * 首页
	 */
	@RequestMapping("/index.html")
	public String home(HttpServletRequest request){
        request.setAttribute("url", HttpUtils.getRequestURL(request));// 网址路径
        request.setAttribute("lang", HttpUtils.getLanguage(request));// 网址路径
		return "index";
	}

	@Resource
	private BulletConfig bulletConfig;


	@Resource
	private ClientVersionService clientVersionService;

    /**
     * 初始化接口
     * @return
     */
	@GetMapping("/api/open/init")
	@ResponseBody
	public R init(){
		String websiteUrl = bulletConfig.getServerUrl();
		String website = websiteUrl.substring(websiteUrl.indexOf("://")+3);
		String version = clientVersionService.getMaxVersion();
        Map map = new HashMap(6);
        map.put("website", website);
        map.put("websiteUrl", websiteUrl);
        map.put("clientVersion", version);
        map.put("serverVersion", "v1.3.0");
        map.put("dockerClientVersion", "0.0.7");
        map.put("apkClientVersion", "0.0.2");
		resourceManager.loadResource();
		return R.success(map);
	}


	@Resource
	private CountService countService;

	/**
	 * 统计数据接口
	 * @return
	 */
	@GetMapping("/api/open/statistics")
	@ResponseBody
	public R statistics(){
		CountVO countVO = countService.getCountInfo();
		return R.success(countVO);
	}

}
