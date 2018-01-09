package com.wuweibi.bullet.client;/**
 * Created by marker on 2017/12/11.
 */

import com.wuweibi.bullet.protocol.MsgHeart;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * 心跳线程
 *
 * @author marker
 * @create 2017-12-11 下午10:34
 **/
public class HeartThread extends TimerTask {

    private Logger logger = LoggerFactory.getLogger(HeartThread.class);


    /** 客户端 */
    private Client client;

    public HeartThread(Client client) {
        this.client = client;
    }


    @Override
    public void run() {

        int id = client.getId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("Connection[{}] heart time={}", id, sdf.format(new Date()));

        try {
            if(client.getSession().isOpen()){
                MsgHeart msgHeart = new MsgHeart();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                msgHeart.write(outputStream);

                // 包装了Bullet协议的
                byte[] resultBytes = outputStream.toByteArray();
                ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                client.getSession().getAsyncRemote().sendBinary(buf);
            }
        } catch (IOException e) {
            logger.error("", e);
        }


    }
}

