package com.wuweibi.bullet.oauth2.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 *
 * 登录策略配置
 * 支持Token策略，登录失败策略配置
 *
 * @author marker
 */
@Slf4j
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth.strategy")
public class SystemAuthStrategyConfig {
	public SystemAuthStrategyConfig(){
		log.debug("SystemAuthStrategyConfig init... ");
	}


	/**
	 * 是否每次重新生成token
	 */
	private Boolean generateNewToken =  Boolean.TRUE;

	/**
	 * 是否复用refresh token
	 */
	private Boolean reuseRefreshToken =  Boolean.FALSE;

	/**
	 * 是否支持刷新token
	 */
	private Boolean supportRefreshToken = Boolean.TRUE;


	/**
	 * 登录失败次数
	 */
	private int loginTotalCount = 5;

	/**
	 * 失败超过限制 等待分钟数
	 */
	private int faildWaitTime = 20;

}
