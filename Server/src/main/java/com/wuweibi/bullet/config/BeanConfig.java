package com.wuweibi.bullet.config;

import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by Administrator on 2019/5/30.
 */
@Configuration
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
