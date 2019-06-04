package com.wuweibi.bullet.client.listener;/**
 * Created by marker on 2019/5/31.
 */

import com.wuweibi.bullet.client.ConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author marker
 * @create 2019-05-31 11:23
 **/
@Slf4j
public class CloseClientListener implements ApplicationListener<ContextClosedEvent> {

    private ConnectionPool pool;
    public CloseClientListener(ConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {

        log.warn("SpringBoot 停止运行中...");
        pool.stop();



    }
}
