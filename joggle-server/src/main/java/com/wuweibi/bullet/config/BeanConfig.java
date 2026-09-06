package com.wuweibi.bullet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wuweibi.bullet.config.properties.AliSmsProperties;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Slf4j
@Configuration
@ComponentScan(
        basePackages = {
                "com.wuweibi.bullet.service",
                "com.wuweibi.bullet.client",
                "com.wuweibi.bullet.oauth2.service",
                "com.wuweibi.bullet.oauth2.manager",
                "com.wuweibi.bullet.business",
                "com.wuweibi.bullet.controller"
        }
)
public class BeanConfig {

    @Resource
    private AliSmsProperties aliSmsProperties;

    /**
     * WebSocket链接池
     */
    @Bean
    public SpringUtils beanSpringUtils() {
        return new SpringUtils();
    }

    /**
     * WebSocket链接池
     */
    @Bean
    public WebsocketPool beanC2oonPool() {
        return new WebsocketPool();
    }


    @Bean
    public TaskExecutor beanTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }





    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    @Bean
    public com.aliyun.dysmsapi20170525.Client createClient( ) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliSmsProperties.getAccessKeyId())
                .setAccessKeySecret(aliSmsProperties.getAccessKeySecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }


    /**
     * redis session 序列化方式
     */
    @Component("springSessionDefaultRedisSerializer")
    public static class SessionSerializer extends GenericJackson2JsonRedisSerializer implements InitializingBean {
        static ObjectMapper objectMapper = new ObjectMapper();
        static {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        }

        public SessionSerializer( ) {
            super(objectMapper);
        }


        @Override
        public void afterPropertiesSet() throws Exception {

        }
    }



}
