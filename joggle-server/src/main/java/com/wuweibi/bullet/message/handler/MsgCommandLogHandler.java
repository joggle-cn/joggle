package com.wuweibi.bullet.message.handler;

import com.wuweibi.bullet.protocol.MsgCommandLog;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import com.wuweibi.bullet.websocket.LogAnnotation;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * 日志消息
 * @author marker on 2025/7/8.
 */
@Service
public class MsgCommandLogHandler implements MessageHandlerStrategy<MsgCommandLog> {
    @Override
    public void handle(MsgCommandLog msg) throws IOException {
        long  deviceId = msg.getDeviceId();

        LogAnnotation.broadcast(deviceId, msg.getLine());
    }
}
