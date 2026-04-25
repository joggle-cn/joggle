package com.wuweibi.bullet.message;

import com.wuweibi.bullet.message.handler.MsgAuthHandler;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 消息处理器上下文
 * @author marker on 2025/7/8.
 *
 *
 *
 * **/
@Component
public class MessageHandlerContext {

    private static final Logger log = LoggerFactory.getLogger(MsgAuthHandler.class);

    @Resource
    private List<MessageHandlerStrategy> strategies;


    public boolean handleMessage(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        try {
            head.read(bis); // 读取消息头
            for (MessageHandlerStrategy<Message> strategy : strategies) {
                if (strategy.supports(head.getCommand())) {
                    // 读取对应的消息类型
                    Class<Message> type = strategy.getMessageType(); // 返回 String.class
                    Message message = type.newInstance();
                    message.setHead(head);
                    message.read(bis); // 读取消息内容
                    strategy.handle(message);
                    return true; // 找到对应的handler并处理消息，返回true
                }
            }
            // 如果没有找到对应的处理器
            throw new UnsupportedOperationException("Unsupported message type: " + head.getCommand());
        } catch (IOException e) {
            log.error("Message handling error", e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(bis);
        }
        return false;
    }
}

