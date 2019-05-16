package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2017/12/11.
 */

import com.wuweibi.bullet.client.BulletClient;
import com.wuweibi.bullet.protocol.MsgBindIP;
import com.wuweibi.bullet.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * 心跳线程
 *
 * @author marker
 * @create 2017-12-11 下午10:34
 **/
public class BindIPThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(BindIPThread.class);


    /** 客户端 */
    private BulletClient client;

    public BindIPThread(BulletClient client) {
        this.client = client;
    }


    @Override
    public void run() {

        Integer id = client.getId();
        if(id == null){
            logger.error("ClientId is ", id);
        }
        String ip = Tools.getIp();

        logger.info("Connection[{}] BindIp IP={}", id, ip);

        try {
            if(client.getSession().isOpen()){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                MsgBindIP msg = new MsgBindIP(ip);
                msg.write(outputStream);

                // 包装了Bullet协议的
                byte[] resultBytes = outputStream.toByteArray();
                ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                client.getSession().getBasicRemote().sendBinary(buf);
            }
        } catch (IOException e) {
            logger.error("", e);
        }


    }
}

