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


    public boolean isActive(){
        return session.isOpen();

    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    /**
     * 打开WebSocket链接
     */
    public void open() {
        count++;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            Client client = new Client();

            this.session = container.connectToServer(client, new URI(this.url)); // 连接会话
            client.setConnection(this);

        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
