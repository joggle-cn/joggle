package com.wuweibi.bullet.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.alias.SessionAttr;
import com.wuweibi.bullet.alias.Var;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.utils.CodeHelper;
import com.wuweibi.bullet.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class AllInterceptor implements HandlerInterceptor {
	
	/** 日志记录器 */ 
	protected Logger logger = LoggerFactory.getLogger(getClass()); 
	

    /** 不需要登录的接口 */
	static String[] NotLoginUrls = new String[]{
        "/api/user/login/"
        , "/api/login"
    };
	
	
	/**
	 * 预处理
	 * （验证API接口是否登录）
	 */
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
		System.out.println(handler);
		HttpSession session = request.getSession(true);
//		Object cookieDeceive = session.getAttribute(SessionAttr.CookieDeceive); 
//		String id = session.getId();
//		String ip = HttpUtils.getRemoteHost(request);
//		System.out.println(id+"-"+ip);
//		String md5 = MD5.getMD5Code(id+"-"+ip);


//		User user = (User) session.getAttribute(SessionAttr.LOGIN_USER);
//        if(user != null){
//            long userId = user.getId();
//            request.setAttribute(Var.UserId, userId);
//        }
		
		
		String uri = request.getRequestURI();
	 
		
//		// 需要邀请验证
//		if(uri.startsWith("/verify/one/yaoqing")){
//			return true;
//		}else{
//			if(!uri.startsWith("/api/")) {
//				Cookie c = CookieUtil.getCookie(request, "yqcode");
//				if(c == null){
//					response.sendRedirect("/verify/one/yaoqing.rain");
//					return false;
//				}
//			}
//		}
//		
//
        // 验证不需要登录的接口
        String url = request.getRequestURI().toString();
        for (String string : NotLoginUrls) {
            if (url.contains(string)) {
                return true;
            }
        }




		// 安全防护
		if(uri.startsWith("/api/")){// 调用的 API
			Object user = session.getAttribute(SessionAttr.LOGIN_USER);

//			if(md5.equals(cookieDeceive)){
                if(user != null){
                    return true; // 已经登录可以正常调用
                }
//			}
			MessageResult msg = MessageFactory.getUserNotLoginError();
			String str = JSON.toJSONString(msg, false);
            response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(str);
			logger.warn("_User Not Login! {}", str);
			return false;
		}
//		
//		// 老师学生Web
//		if(uri.startsWith("/teacher")|| uri.startsWith("/student")){
//			Object user = session.getAttribute(SessionAttr.LOGIN_USER); 
//			if(md5.equals(cookieDeceive)){
//				if(user != null){
//					return true; // 已经登录可以正常调用  
//				}  
//			}
//			response.sendRedirect("/login.rain"); 
//			return false;
//		} 
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
		
		// 打印请求参数
		CodeHelper.printRequestParameter(request);
		
		
	}
	
	public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		
	}

}
