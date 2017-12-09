package com.wuweibi.bullet.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class CodeHelper {

	

	
	
	/**
	 * 打印参数
	 * @param request
	 */
	public static void printRequestParameter(HttpServletRequest request){
		Enumeration<String> e = request.getParameterNames(); 
		System.out.println("-------------------------------");
		System.out.println(request.getRequestURI());
		System.out.println("请求参数：");
		while(e.hasMoreElements()) { 
			String para = e.nextElement(); 
			System.out.print(para + "=" + request.getParameter(para) +"\n");
		}
		System.out.println("-------------------------------");
	} 

}
