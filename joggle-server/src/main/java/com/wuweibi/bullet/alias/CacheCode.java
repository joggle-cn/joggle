package com.wuweibi.bullet.alias;


/**
 * 缓存码管理
 * 
 * （将统一管理缓存码，为防止名称冲突故将方法名与参数混合md5加密得出前缀码）
 * 
 * @author marker
 * @version 1.0
 */
public interface CacheCode {

	
	
	// 检查更新
	String checkUpdateVersion = "'915864166C5F4F9B'  + #version";
	
	
	// 查看用户解析
	String getUserResolve = "'DCFE019672590A72' + #userId + #practiceId";
	
	
	/** 学校列表缓存 */
	String getSchoolByName = "'F11B8E803217CAD9' + #name";


	/**
	 * 统计今日设备映射流量in + out  (KB)
	 */
	String DEVICE_MAPPING_STATISTICS_FLOW_TODAY = "device:flowCount:%s:bytes";

	/**
	 * 统计今日设备映射流量 链接数
	 */
	String DEVICE_MAPPING_STATISTICS_LINK_TODAY = "device:flowCount:%s:link";

	
	
	
	
	
}
