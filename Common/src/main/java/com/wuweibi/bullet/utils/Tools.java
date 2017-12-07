package com.wuweibi.bullet.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类
 * @author marker
 * @version 1.0
 * @date 2012-10-14
 * */
public class Tools {

	//序列日期生成器格式化工具
	private static SimpleDateFormat sequenceDate = new SimpleDateFormat("mmddhhmmss");
	
	
	/**
	 * 获取序列格式化日期
	 * @return long 日期格式：mmddhhmmss
	 * */
	public static long getCurrentSequenceDate(){
		return Long.valueOf(Tools.sequenceDate.format(new Date()));
	}
}
