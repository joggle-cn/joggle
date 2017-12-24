package com.wuweibi.bullet.client;
/**
 * Created by marker on 2017/11/22.
 */

import com.wuweibi.bullet.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Timer;

/**
 *
 * WebSocket客户端监听
 *
 * @author marker
 * @create 2017-11-22 下午10:48
 **/
@ClientEndpoint()
public class Client {
    /** 日志记录器 */
    protected Logger logger = LoggerFactory.getLogger(Client.class);

    /** 心跳定时器 */
    protected Timer timer = new Timer();

    /** 会话 */
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.info("Connected to endpoint: " + session.getBasicRemote());

        // 启动一个线程做心跳配置
        HeartThread task = new HeartThread(session);
        timer.schedule(task, 5000, 20000);


    }


    /**
     * 接受到 请求新消息，
     * @param message
     */
    @OnMessage
    public void onMessage(ByteBuffer message) {
        byte[] bytes = message.array();

        SocketThread socketThread = new SocketThread(new WebSocketClientProxyImpl(session), bytes);

        socketThread.start();
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose( Session session, CloseReason closeReason) throws InterruptedException {
        logger.error("{} {}", closeReason.toString(), "链接已关闭" );

        logger.error("正在取消心跳线程...");
        timer.cancel();



        Thread.sleep(3000L);
        logger.error("正在重启链接服务器...");
        String url = ConfigUtils.getTunnel() +"/"+ ConfigUtils.getDeviceId();
        System.out.println(url);

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