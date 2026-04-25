package com.wuweibi.bullet.message.handler;

import com.wuweibi.bullet.protocol.MsgAuth;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 *
 * joggle客户端认证消息
 *
 * @author marker
 * @create 2025-07-08 下午1:13
 **/
@Service
public class MsgAuthHandler implements MessageHandlerStrategy<MsgAuth> {

    private static final Logger log = LoggerFactory.getLogger(MsgAuthHandler.class);

    public MsgAuthHandler() {
    }

    @Override
    public void handle(MsgAuth msg) throws IOException {

    }
}
