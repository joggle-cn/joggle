package com.wuweibi.bullet.client;
/**
 * Created by marker on 2017/11/22.
 */

import com.wuweibi.bullet.client.service.CommandThreadPool;
import com.wuweibi.bullet.client.service.SpringUtil;
import com.wuweibi.bullet.client.threads.BindIPThread;
import com.wuweibi.bullet.client.threads.HeartThread;
import com.wuweibi.bullet.client.threads.SocketThread;
import com.wuweibi.bullet.client.websocket.WebSocketClientProxyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 *
 * WebSocket客户端监听
 *
 * @author marker
 * @create 2017-11-22 下午10:48
 **/
@ClientEndpoint()
public class BulletClient {

    /** 日志记录器 */
    protected Logger logger = LoggerFactory.getLogger(BulletClient.class);

    /** 心跳定时器 */
    protected Timer timer;

    /** 会话 */
    private Session session;

    /** 链接 */
    private Connection connection;

    static List<CloseReason.CloseCode> code ;

    static{
        code = new ArrayList<>();
        code.add(CloseReason.CloseCodes.NORMAL_CLOSURE);
//        code.add(CloseReason.CloseCodes.CLOSED_ABNORMALLY);

    }


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.debug("Connected to endpoint({}): successs!", session.getId());

        session.setMaxBinaryMessageBufferSize(101024000);
        session.setMaxIdleTimeout(0);

        // 发送IP
        BindIPThread task2 = new BindIPThread(this);
        task2.start();


        // 启动一个线程做心跳配置
        HeartThread task = new HeartThread(this);
        timer = new Timer();
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
    public void onClose(Session session, CloseReason closeReason) throws InterruptedException {
        logger.error("{}", closeReason.toString() );
        int id = connection.getId();
        CloseReason.CloseCode closeCode = closeReason.getCloseCode();


        logger.debug("Connection[{}] 正在取消心跳线程...", id);
        if(timer != null){
            timer.cancel();
        }


        logger.debug("Connection[{}] 正在检查链接配置...", id);
        if(connection != null){
            // 关闭所有的线程

            CommandThreadPool pool = SpringUtil.getBean(CommandThreadPool.class);

            logger.debug("Connection[{}] 关闭所有的命令线程...", id);
            pool.killAll();

            // 不是正常关闭的情况 重新启动链接
            if(!code.contains(closeCode)){
                Thread.sleep(3000L);
                logger.debug("Connection[{}] 正在重启链接服务器...", id);
                connection.opeAngain();
            }
        } else {
            logger.error("Connection[{}] 对象找不到！", id);
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }




    public Session getSession() {
        return this.session;
    }

    /**
     * 获取 连接 ID
     * @return
     */
    public Integer getId() {
        if(this.connection != null){
            return this.connection.getId();
        }else{
            logger.error("{}", this.connection );
        }
        return null;
    }
}