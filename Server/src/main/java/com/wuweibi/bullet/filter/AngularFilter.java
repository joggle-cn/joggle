package com.wuweibi.bullet.filter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * 系统前端核心过滤器 
 * 
 * @author marker
 * 
 * 
 * */
public class AngularFilter implements Filter {
	
	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(AngularFilter.class); 
	 
	 
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;

		String indexPage = req.getRealPath("/index.html");
		// 检查是浏览器访问还是手机
//		if(checkMoible(request.getHeader("User-Agent"))){
//            indexPage = req.getRealPath("/mobile.html");
//		}

        if(request.getRequestURI().indexOf("/mobi") != -1){
            indexPage = req.getRealPath("/mobile.html");
        }



		 
		PrintWriter pw = null;
		BufferedReader br = null;
		 try {
             
	            //中文乱码
	            res.setContentType("text/html;charset=utf-8");
	             
	              pw = res.getWriter();
	          
	            //创建一个FileReader
	            br = new BufferedReader(new FileReader(indexPage));
	                
	            String txt = null;
	            while((txt = br.readLine())!= null){
	            	pw.write(txt+"\n");
	            }
	            pw.flush(); 
	         } catch (Exception ex) {
	             
	            ex.printStackTrace();
	          
	         }finally{
	             //一定要关闭文件流    
	        	 IOUtils.closeQuietly(br);
	        	 IOUtils.closeQuietly(pw);
	         } 
		
	}



	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.info("angular filter initing..."); 
		 
	}

	
	@Override
	public void destroy() { 
		logger.info("angular filter stoping..."); 
	}




	// b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
	// 字符串在编译时会被转码一次,所以是 "\b"
	// B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
	static String phoneReg = "(iPhone)";
	static String tableReg = "\b(ipad|tablet|(Nexus 7)|up.browser"
			+"|[1-4][0-9]{2}x[1-4][0-9]{2})\b";

	//移动设备正则匹配：手机端、平板
	static Pattern phonePat = Pattern.compile(phoneReg);
	static Pattern tablePat = Pattern.compile(tableReg);

	/**
	 * 检测是否是移动设备访问
	 *
	 * @Title: check
	 * @Date : 2014-7-7 下午01:29:07
	 * @param userAgent 浏览器标识
	 * @return true:移动设备接入，false:pc端接入
	 */
	public static boolean checkMoible(String userAgent){
		if(null == userAgent){
			userAgent = "";
		}
		// 匹配
		Matcher matcherPhone = phonePat.matcher(userAgent);
		Matcher matcherTable = tablePat.matcher(userAgent);
		if(matcherPhone.find() || matcherTable.find()){
			return true;
		} else {
			return false;
		}
	}
}
