package com.wuweibi.bullet.listener;
/**
 * Created by marker on 2019/5/31.
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * 应用停止监听
 * @author marker
 * @create 2019-05-31 11:23
 **/
@Slf4j
public class CloseServerListener implements ApplicationListener<ContextClosedEvent> {

    public CloseServerListener( ) {  }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.warn("SpringBoot 停止中...");
    }
}
