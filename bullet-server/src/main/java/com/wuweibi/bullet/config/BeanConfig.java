package com.wuweibi.bullet.config;

import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Slf4j
@Configuration
@ComponentScan(
    basePackages={
        "com.wuweibi.bullet.service",
        "com.wuweibi.bullet.client",
        "com.wuweibi.bullet.oauth2.service",
        "com.wuweibi.bullet.oauth2.manager",
        "com.wuweibi.bullet.business",
        "com.wuweibi.bullet.controller"
    }
)
public class BeanConfig {


    /**
     * WebSocket链接池
     */
    @Bean
    public SpringUtils beanSpringUtils(){
        return new SpringUtils();
    }

    /**
     * WebSocket链接池
     */
    @Bean
    public CoonPool beanCoonPool(){
        return new CoonPool();
    }



    @Bean
    public TaskExecutor beanTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }




}
