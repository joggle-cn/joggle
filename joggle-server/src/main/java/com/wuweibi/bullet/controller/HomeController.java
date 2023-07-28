package com.wuweibi.bullet.controller;

import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.domain.vo.CountVO;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.oauth2.manager.ResourceManager;
import com.wuweibi.bullet.service.CountService;
import com.wuweibi.bullet.system.client.service.ClientVersionService;
import com.wuweibi.bullet.utils.HttpUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


	@Resource
	private WebsocketPool websocketPool;

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
        map.put("serverVersion", "v1.3.3");
        map.put("dockerClientVersion", "0.0.14");
        map.put("apkClientVersion", "1.3.3");
        map.put("websocketConn", websocketPool.getInfo());
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



	/**
	 * OAuth登录
	 * @return
	 */
	@ApiOperation("OAuth登录")
	@PostMapping("/oauth/token1")
	public R<Boolean> login(@RequestHeader("Authorization") String authorization, LoginForm loginForm){
		return null;
	}


	@Data
	public static class LoginForm {
		@ApiModelProperty(value = "账号")
		private String username;

		@ApiModelProperty(value = "密码")
		private String password;

		@ApiModelProperty(value = "授权类型", example="password" )
		private String grant_type = "password";
	}

}
