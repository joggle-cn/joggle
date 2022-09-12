package com.wuweibi.bullet.config;


import com.wuweibi.bullet.service.DomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableScheduling
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {


    @Resource
    private DomainService domainService;


    @Bean()
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }




    /**
     * 每10秒点执行检查是否过期
     */
    @Scheduled(fixedRate = 1000*10)
    public void work() {
        // task execution logic
        domainService.checkStatus();
    }
}
