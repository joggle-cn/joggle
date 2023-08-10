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

    @Resource
    private UserPackageManager userPackageManager;


    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }


    /**
     * 每10秒点执行检查域名是否过期，过期域名，自动关闭映射
     */
    @Scheduled(fixedRate = 1000*10)
    public void checkStatus() {
        domainService.checkStatus();
    }


    /**
     * 用户认证自动化审核 每60秒执行检查
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void work4UserCertificationProgress() {
        userCertificationTaskService.progress();
    }


    /**
     * 域名到期超过2天未续费的资源释放。
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void resourceDueTimeRelease() {
        domainService.resourceDueTimeRelease();
    }


    /**
     * VIP用户资源包到期释放 10分钟一次
     * （到期立即释放）
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void userPackageRelease() {
        userPackageManager.expireFree();
    }

    /**
     * VIP用户资源包到期前2天提醒，每日9点执行一次
     */
    @Scheduled(cron = "0 0 9 * * ? ")
    public void userPackageExpirationReminder() {
        userPackageManager.taskUserPackageExpirationReminder();
    }


    /**
     * 发放套餐流量（每月1日 00:00:00）
     * email提醒
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetFlow() {
        userPackageManager.resetPackageFlow();
    }



}
