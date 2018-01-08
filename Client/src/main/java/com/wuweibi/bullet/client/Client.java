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

    /** 链接 */
    private Connection connection;



    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.info("Connected to endpoint: " + session.getBasicRemote());

        // 启动一个线程做心跳配置
        HeartThread task = new HeartThread(session);
        timer.schedule(task, 5000, 10000);


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


        logger.info("正在检查链接配置...");
        if(connection != null){
            Thread.sleep(3000L);
            logger.error("正在重启链接服务器...");
            connection.open();

        } else {
            logger.error("Connection 对象找不到！");
        }




    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}