package com.wuweibi.bullet.client;
/**
 * Created by marker on 2017/11/22.
 */

import com.wuweibi.bullet.client.service.CommandThreadPool;
import com.wuweibi.bullet.client.service.SpringUtil;
import com.wuweibi.bullet.client.threads.BindIPThread;
import com.wuweibi.bullet.client.threads.HeartThread;
import com.wuweibi.bullet.client.threads.SocketThread;
import com.wuweibi.bullet.client.utils.ConfigUtils;
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

    static List<CloseReason.CloseCode> code = new ArrayList<>();

    static{
        code.add(CloseReason.CloseCodes.NORMAL_CLOSURE);
        code.add(CloseReason.CloseCodes.NOT_CONSISTENT);
    }


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.debug("Connected to endpoint({}): conn successs", session.getId());

        session.setMaxBinaryMessageBufferSize(101024000);
        session.setMaxIdleTimeout(0);

        // 等待5秒再启动线程
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            logger.error("", e);
        }

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
        CloseReason.CloseCode closeCode = closeReason.getCloseCode();

        logger.debug("Connection 正在取消心跳线程...");
        if(timer != null){
            timer.cancel();
        }
        if(connection != null){
            // 关闭所有的线程
            CommandThreadPool pool = SpringUtil.getBean(CommandThreadPool.class);
            logger.debug("Connection 关闭所有的命令线程...");
            pool.killAll();

            // 不是正常关闭的情况 重新启动链接
            if(!code.contains(closeCode)){// 除了1000 、1007 以外的 执行
                Thread.sleep(3000L);
                logger.debug("Connection 正在重启链接服务器...");
                connection.openAgain();
            } else if(CloseReason.CloseCodes.NOT_CONSISTENT.equals(closeCode)) {// 由于设备已经在线（不符合约定)
                logger.error("========================================================");
                logger.error("= 客户端启动失败!!! \t");
                logger.error("= 请将deviceNo配置为\"null\"，以便服务器分配新的设备编号\t");
                logger.error("= 修改配置文件您不必重启应用，程序会自动重新加载配置信息的.\t");
                logger.error("========================================================");
                logger.debug("等待30秒再次请求链接...");
                Thread.sleep(30000L);

                logger.debug("重新加载一次配置信息...");
                ConfigUtils.reload();

                connection.openAgain();
            } else { //
                logger.warn("========================================================");
                logger.warn("= 正常关闭应用!!! \t");
                logger.warn("========================================================");

            }
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




    public Session getSession() {
        return this.session;
    }


}