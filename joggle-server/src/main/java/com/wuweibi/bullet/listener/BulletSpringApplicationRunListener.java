package com.wuweibi.bullet.listener;

import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;


/**
 * 启动状态监听
 *
 * @author marker
 */
@Slf4j
public class BulletSpringApplicationRunListener implements SpringApplicationRunListener {

    //必须有的构造器
    public BulletSpringApplicationRunListener(SpringApplication application, String[] args) {
    }


    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("SpringBoot contextLoaded...");
    }


    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.started(context, timeTaken);
        log.info("SpringBoot 启动完成...");
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
        log.info("SpringBoot 初始化设备状态...");
        deviceOnlineService.allDownNow();
        log.info("SpringBoot 数据库记录设备已经下线...");

//        CoonPool coonPool = SpringUtils.getBean(CoonPool.class);
//        coonPool.stop();
    }


    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("SpringBoot 启动失败...");
    }


}
