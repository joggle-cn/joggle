

package com.wuweibi.bullet.cache.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.AllArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import javax.annotation.Resource;

/**
 * RedisTemplate 配置
 *
 * @author marker
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConnectionConfig {


	@Resource
	private RedisProperties redisProperties;


	/**
	 * 默认的Redis链接 db0
	 * @return
	 */
	@Primary
	@Bean(name = "mainRedisConnectionFactory")
	public RedisConnectionFactory newMainRedisConnectionFactory() {
		return createRedisConnectionFactory(0);
	}




	/**
	 * 用于缓存的链接
	 * @return
	 */
	@Bean(name = "cacheRedisConnectionFactory")
	public RedisConnectionFactory newCacheRedisConnectionFactory() {
		return createRedisConnectionFactory(1);
	}


	/**
	 * 创建redis连接工厂
	 */
	public LettuceConnectionFactory createRedisConnectionFactory(int dbIndex) {
		String host = redisProperties.getHost();
		int port = redisProperties.getPort();
		ClientOptions clientOptions = ClientOptions.builder()
				.disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
				.autoReconnect(true)
				.socketOptions(SocketOptions.builder().keepAlive(true).build())
				.build();

		RedisProperties.Pool pool = redisProperties.getLettuce().getPool();

		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
		genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
		genericObjectPoolConfig.setMaxTotal(pool.getMaxActive());
		genericObjectPoolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
//		genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRuns().toMillis());

		LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
				.clientOptions(clientOptions)
				.poolConfig(genericObjectPoolConfig)
				.build();


		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
		redisStandaloneConfiguration.setDatabase(dbIndex);
		redisStandaloneConfiguration.setPassword(redisProperties.getPassword());

		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);

		return connectionFactory;
	}






}
