package com.wuweibi.bullet.listener;

import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class BulletSpringApplicationRunListener implements SpringApplicationRunListener {

    //必须有的构造器
    public BulletSpringApplicationRunListener(SpringApplication application, String[] args){
    }

    @Override
    public void starting() {
        System.out.println("SpringApplicationRunListener...starting...");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        Object o = environment.getSystemProperties().get("os.name");
        System.out.println("SpringApplicationRunListener...environmentPrepared.."+o);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...contextPrepared...");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...contextLoaded...");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...started...");


        log.warn("SpringBoot 启动完成...");
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
        log.warn("SpringBoot 初始化设备状态...");
        deviceOnlineService.allDownNow();

        CoonPool coonPool = SpringUtils.getBean(CoonPool.class);
        coonPool.stop();


    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...running...");

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("SpringApplicationRunListener...failed...");

    }


}
