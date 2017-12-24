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

    private Session session;

    public HeartThread(Session session) {
        this.session = session;
    }


    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("heart time={}", sdf.format(new Date()));

        try {
            if(session.isOpen()){
                MsgHeart msgHeart = new MsgHeart();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                msgHeart.write(outputStream);

                // 包装了Bullet协议的
                byte[] resultBytes = outputStream.toByteArray();
                ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                session.getAsyncRemote().sendBinary(buf);
            }
        } catch (IOException e) {
            logger.error("", e);
        }


    }
}

