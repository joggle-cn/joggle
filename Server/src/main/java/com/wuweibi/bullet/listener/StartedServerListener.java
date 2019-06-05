package com.wuweibi.bullet.listener;/**
 * Created by marker on 2019/5/31.
 */

import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

/**
 * 应用停止监听
 *
 * @author marker
 * @create 2019-05-31 11:23
 **/
@Slf4j
public class StartedServerListener implements ApplicationListener<ContextStartedEvent> {

    public StartedServerListener( ) {  }


    @Override
    public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {
        log.warn("SpringBoot 启动完成...");

        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);

        log.warn("SpringBoot 初始化设备状态...");
        deviceOnlineService.allDownNow();

        CoonPool coonPool = SpringUtils.getBean(CoonPool.class);
        coonPool.stop();
    }
}
