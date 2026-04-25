package com.wuweibi.bullet.message.handler;

import com.wuweibi.bullet.protocol.MsgProxy;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 *
 * joggle代理消息处理
 *
 * @author marker
 * @create 2025-07-08 下午1:13
 **/
@Service
public class MsgProxyHandler implements MessageHandlerStrategy<MsgProxy> {

    private static final Logger log = LoggerFactory.getLogger(MsgProxyHandler.class);

    public MsgProxyHandler() {
    }

    @Override
    public void handle(MsgProxy msg) throws IOException {

    }
}
