package com.wuweibi.bullet.config;

import com.wuweibi.bullet.websocket.BulletAnnotation;
import com.wuweibi.bullet.websocket.LogAnnotation;
import com.wuweibi.bullet.websocket.VideoAnnotation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置
 *
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    /**
     * Bullet WebSocket接口
     * @return
     */
    @Bean
    public BulletAnnotation serverEndpointBulletAnnotation() {
        return new BulletAnnotation();
    }



    /**
     * Video WebSocket接口
     * @return
     */
    @Deprecated
    @Bean
    public VideoAnnotation serverEndpointVideoAnnotation() {
        return new VideoAnnotation();
    }

    @Bean
    public LogAnnotation serverEndpointVideoAnnotatio2n() {
        return new LogAnnotation();
    }
}
