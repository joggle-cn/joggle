package com.wuweibi.bullet.client;/**
 * Created by marker on 2018/1/8.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

/**
 * @author marker
 * @create 2018-01-08 下午8:15
 **/
public class Connection {

    private Logger logger = LoggerFactory.getLogger(Connection.class);

    private String url;

    // WebSocket session
    private Session session;

    /** 链接ID */
    private int id;


    /** 重试链接次数 */
    private int count = 0;

    public Connection(Session session) {
        this.session = session;
    }

    public Connection(String url) {
        this.url = url;
    }


    /**
     * 是否活跃
     * @return
     */
    public boolean isActive(){
        return session.isOpen();
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    WebSocketContainer container = ContainerProvider.getWebSocketContainer();


    /**
     * 打开WebSocket链接
     */
    public void open() throws Exception {
        Client client = new Client();
        this.session = container.connectToServer(client, new URI(this.url)); // 连接会话
        client.setConnection(this);

    }


    /**
     * 再次打开连接
     * （等待30秒，重试次数20次）
     */
    public void opeAngain() {
        try {
            count++;
            logger.debug("Connection[{}] 第{}次尝试连接", id, count);
            if (count <= 20){ // 重试次数10次。
                this.open();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            // 等地啊3秒
            try {
                Thread.sleep(30000L);
            } catch (InterruptedException e1) {}
            if (count <= 20) { // 重试次数10次。
                opeAngain();
            }
        }
    }
}
