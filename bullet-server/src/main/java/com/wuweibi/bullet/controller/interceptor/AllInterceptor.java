package com.wuweibi.bullet.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.oauth2.service.AuthenticationService;
import com.wuweibi.bullet.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;



public class AllInterceptor implements HandlerInterceptor {

	/**
	 * 响应编码
	 */
	public static final String encoding = "UTF-8";
	/**
	 * 日志记录器
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 预处理
	 * （验证API接口是否登录）
	 */
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI().toString();


        // 访问的首页地址
        if("/".equals(url)){
        	return true;
		}

        // 验证权限
		AuthenticationService authenticationService = SpringUtils.getBean(AuthenticationService.class);

		// 开放接口校验
		if (authenticationService.ignoreAuthentication(url)) {
			return true;
		}

		// 接口权限校验
//		Result result = authenticationService.hasPermission(request);
//		if(result.isSuccess()){
//			return true;
//		}


		// 无权限 返回请登录

//		renderJson(result);
		return true;
	}


	/** 处理请求 */
	public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//		/* 基础信息收集 */
//		String ip = HttpUtils.getRemoteHost(request);// IP地址获取
//		request.setAttribute(Var.REAL_IP, ip);// 将用户真实IP写入请求属性
//		request.setAttribute(Var.WEB_APP_URL, HttpUtils.getRequestURL(request));// 网址路径
//		request.setAttribute(Var.WEB_APP_LANG, HttpUtils.getLanguage(request));// 网址路径
//
//
//
//		// 打印请求参数
//		CodeHelper.printRequestParameter(request);


	}

	public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
			throws Exception {


	}



	/**
	 * 警告信息
	 * @throws IOException
	 */
	private void renderJson(Result result) throws IOException {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getResponse();


		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		response.setContentType("application/json;charset=UTF-8");
		Writer writer = response.getWriter();
		writer.write(JSON.toJSONString(result));
		writer.flush();
		writer.close();
	}
}
