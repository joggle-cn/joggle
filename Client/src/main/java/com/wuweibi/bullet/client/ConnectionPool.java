package com.wuweibi.bullet.client;

/**
 * Created by marker on 2018/1/8.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * 连接池
 *
 * @author marker
 * @create 2018-01-08 下午8:06
 **/
public class ConnectionPool {

    private Logger logger = LoggerFactory.getLogger(ConnectionPool.class);




    private List<Connection> list = new ArrayList<>();


    public ConnectionPool(){

    }

    /**
     * 启动连接池
     */
    public void startup(){

        try {
            Connection connection = new Connection();

            Thread.sleep(100L);

            connection.open(); // 打开WebSocket链接

            list.add(connection);

        } catch (Exception e) {
            logger.error("", e);
        }

    }

    /**
     * 停止所有连接
     *
     */
    public void stop(){

        Iterator<Connection> it = list.iterator();
        while (it.hasNext()){
            Connection connection = it.next();

            connection.stop();

        }
    }


}
