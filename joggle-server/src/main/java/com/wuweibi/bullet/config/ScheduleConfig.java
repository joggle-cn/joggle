package com.wuweibi.bullet.config;


import com.wuweibi.bullet.business.UserDomainCertBiz;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.task.UserCertificationTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@EnableScheduling
@RestController
@RequestMapping("/inner/open/schedule")
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
        return Executors.newScheduledThreadPool(2);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }


    /**
     * 每60秒执行检查域名是否过期，过期域名，自动关闭映射
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void checkStatus() {
        try {
            domainService.checkStatus();
        } catch (Exception e) {
            log.error("[定时任务] 域名过期检查异常", e);
        }
    }


    /**
     * 用户认证自动化审核 每60秒执行检查
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void work4UserCertificationProgress() {
        try {
            userCertificationTaskService.progress();
        } catch (Exception e) {
            log.error("[定时任务] 用户认证审核异常", e);
        }
    }


    /**
     * 域名到期超过2天未续费的资源释放。
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void resourceDueTimeRelease() {
        try {
            domainService.resourceDueTimeRelease();
        } catch (Exception e) {
            log.error("[定时任务] 资源到期释放异常", e);
        }
    }


    /**
     * VIP用户资源包到期释放 10分钟一次
     * （到期立即释放）
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void userPackageRelease() {
        try {
            userPackageManager.expireFree();
        } catch (Exception e) {
            log.error("[定时任务] 用户资源包到期释放异常", e);
        }
    }

    /**
     * VIP用户资源包到期前2天提醒，每日9点执行一次
     */
    @Operation(summary = "VIP用户资源包到期前2天提醒")
    @PostMapping("/package/expiration/reminder")
    @Scheduled(cron = "0 0 9 * * ? ")
    public void userPackageExpirationReminder() {
        try {
            userPackageManager.taskUserPackageExpirationReminder();
        } catch (Exception e) {
            log.error("[定时任务] 资源包到期提醒异常", e);
        }
    }


    /**
     * 发放套餐流量（每月1日 00:00:00）
     * email提醒
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetFlow() {
        try {
            userPackageManager.resetPackageFlow();
        } catch (Exception e) {
            log.error("[定时任务] 发放套餐流量异常", e);
        }
    }

    @Resource
    private UserDomainCertBiz userDomainCertBiz;

    /**
     * 域名证书续期 10分钟一次
     *
     */
    @Scheduled(fixedRate = 1000 * 60 *  10)
    public void domainCertReNewTask() {
        try {
            userDomainCertBiz.startCertReNewTask();
        } catch (Exception e) {
            log.error("[定时任务] 域名证书续期异常", e);
        }
    }

}
