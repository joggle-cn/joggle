package com.wuweibi.bullet.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Enumeration;

@Slf4j
public class CodeHelper {





	

	
	
	/**
	 * 打印参数
	 * @param request
	 */
	public static void printRequestParameter(HttpServletRequest request){
		Enumeration<String> e = request.getParameterNames(); 
		log.debug("-------------------------------");
        log.debug("uri: {}", request.getRequestURI());

		while(e.hasMoreElements()) { 
			String para = e.nextElement(); 
			System.out.print(para + "=" + request.getParameter(para) +"\n");
		}
        log.debug("params：{}", request.getParameterMap() );
		System.out.println("-------------------------------");
	}

	static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);


	/**
	 * 获取雪花
	 * @return
	 */
	public static SnowflakeIdWorker getIdWorker() {
		return idWorker;
	}

	/**
	 * 创建一个设备编号
	 * @return
	 */
	public static String makeNewCode() {
		Long id = getIdWorker().nextId();
		BigInteger number = new BigInteger(String.valueOf(id));
		// 生成设备编号
		return number.toString(Character.MAX_RADIX);
	}
}
