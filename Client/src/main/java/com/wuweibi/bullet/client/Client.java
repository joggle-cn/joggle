package com.wuweibi.bullet.client;/**
 * Created by marker on 2017/11/22.
 */

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 *
 * WebSocket客户端监听
 *
 * @author marker
 * @create 2017-11-22 下午10:48
 **/
@ClientEndpoint()
public class Client {

    /** 会话 */
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void onMessage(ByteBuffer message) {
        byte[] bytes = message.array();

        SocketThread socketThread = new SocketThread(new WebSocketClientProxyImpl(session), bytes);
        socketThread.setHost("localhost");
        socketThread.setPort(8080);

        socketThread.start();
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose( Session session, CloseReason closeReason) {
        System.out.println(closeReason.toString());
        System.out.println("链接已关闭");

        String url = "ws://localhost:8082/ws/chat/marker";


        WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");


        try {
            container.connectToServer(Client.class, new URI(url)); // 连接会话
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
}