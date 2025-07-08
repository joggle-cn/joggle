package com.wuweibi.bullet.message.handler;

import com.wuweibi.bullet.protocol.MsgHeart;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 *
 * joggle 心跳
 *
 * @author marker
 * @create 2025-07-08 下午1:13
 **/
@Service
public class MsgHeartHandler implements MessageHandlerStrategy<MsgHeart> {

    private static final Logger log = LoggerFactory.getLogger(MsgHeartHandler.class);

    public MsgHeartHandler() {
    }

    @Override
    public void handle(MsgHeart msg) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            msg.write(outputStream);
            // 包装了Bullet协议的
            byte[] resultBytes = outputStream.toByteArray();
            ByteBuffer buf = ByteBuffer.wrap(resultBytes);
            // TODO 待确认
//            this.getSession().getBasicRemote().sendPong(buf);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
