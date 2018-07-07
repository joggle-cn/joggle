package com.wuweibi.bullet.client;/**
 * Created by marker on 2018/1/8.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * 连接池
 *
 * @author marker
 * @create 2018-01-08 下午8:06
 **/
public class ConnectionPool {

    private Logger logger = LoggerFactory.getLogger(ConnectionPool.class);


    private int poolSize = 1;


    private Properties config;


    private List<Connection> list = new ArrayList<>();


    public ConnectionPool(){

    }

    private void init(){



    }


    /**
     * 启动连接池
     */
    public void startup(){
        // 通道服务器
        String tunnel = this.config.getProperty("tunnel");
        // 设备ID
        String deviceId =  this.config.getProperty("deviceId");


        String url = tunnel + "/" + deviceId;

        try {
            for(int i = 0; i < poolSize; i++){
                // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");

                String url2 = url + "/" + i;
                logger.info("{}", url2);

                Connection connection = new Connection(url2);
                connection.setId(i);

                Thread.sleep(100L);

                connection.open(); // 打开WebSocket链接

                list.add(connection);

            }
        } catch (Exception e) {
            logger.error("", e);
        }

    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}
