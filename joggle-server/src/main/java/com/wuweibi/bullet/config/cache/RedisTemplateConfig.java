

package com.wuweibi.bullet.config.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * RedisTemplate 配置
 *
 * @author marker
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@AutoConfigureAfter(RedisConnectionConfig.class)
@Import(RedisConnectionConfig.class)
public class RedisTemplateConfig {

	public static final String BEAN_REDIS_TEMPLATE = "redisTemplate";


	@Resource(name = "mainRedisConnectionFactory")
	private RedisConnectionFactory redisConnectionFactory;


	@Bean(name = RedisTemplateConfig.BEAN_REDIS_TEMPLATE)
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		// Jackson2JsonRedisSerializer
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
		//序列化时添加对象信息
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(objectMapper);


		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Bean(name = "stringRedisTemplate")
	public StringRedisTemplate strnigRedisTemplate() {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();

		// Jackson2JsonRedisSerializer
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
		//序列化时添加对象信息
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(objectMapper);

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

}
