package com.wuweibi.bullet.client;

/**
 * Created by marker on 2017/12/11.
 */

import com.wuweibi.bullet.ConfigUtils;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;

/**
 * 启动主要运行类
 *
 * (目前采用单个长链接，且没有重试机制)
 *
 *
 * @author marker
 * @create 2017-12-11 下午9:08
 **/
public class Main {


    public static void main(String[] args) throws URISyntaxException, IOException, DeploymentException, InterruptedException {


        String url = ConfigUtils.getTunnel() +"/"+ ConfigUtils.getDeviceId();
        System.out.println(url);


        WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");

        Session session = container.connectToServer(Client.class, new URI(url)); // 连接会话




        while (true){
            Thread.sleep(3000000L);
        }

    }


}
