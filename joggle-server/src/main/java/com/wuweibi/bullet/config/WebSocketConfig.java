package com.wuweibi.bullet.config;

import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import com.wuweibi.bullet.websocket.LogAnnotation;
import com.wuweibi.bullet.websocket.VideoAnnotation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

/**
 * WebSocket 配置
 *
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Resource
    private BulletConfig config;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


//    /**
//     * Bullet WebSocket接口
//     * @return
//     */
//    @Bean
//    public BulletAnnotation serverEndpointBulletAnnotation() {
//        return new BulletAnnotation();
//    }


    @Bean
    public Bullet3Annotation serverEndpointBullet3Annotation() {
        return new Bullet3Annotation();
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
