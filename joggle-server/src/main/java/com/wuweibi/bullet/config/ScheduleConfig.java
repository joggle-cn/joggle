package com.wuweibi.bullet.config;


import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.task.UserCertificationTaskService;
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

    @Resource
    private UserCertificationTaskService userCertificationTaskService;



    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }


    /**
     * 每10秒点执行检查是否过期，过期域名，自动关闭映射
     */
    @Scheduled(fixedRate = 1000*10)
    public void checkStatus() {
        domainService.checkStatus();
    }


    /**
     * 每60秒执行检查 用户认证自动化检查
     */
    @Scheduled(fixedRate = 1000*60)
    public void work4UserCertificationProgress() {
        userCertificationTaskService.progress();
    }


    /**
     * 到期超过2天未续费的资源释放。
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void resourceDueTimeRelease() {
        domainService.resourceDueTimeRelease();
    }

    @Resource
    private UserPackageManager userPackageManager;

    /**
     * VIP用户资源包释放 10分钟一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void userPackageRelease() {
        userPackageManager.expireFree();
    }


    /**
     * 发放套餐流量（每月1日 00:00:00）
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetFlow() {
        userPackageManager.resetPackageFlow();
    }



}
