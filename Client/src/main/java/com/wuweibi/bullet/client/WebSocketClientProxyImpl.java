package com.wuweibi.bullet.client;
/**
 * Created by marker on 2017/11/20.
 */


import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * 代理
 *
 * @author marker
 * @create 2017-11-20 下午1:12
 **/
public class WebSocketClientProxyImpl implements WebSocketClientProxy {


    private Session session;




    public WebSocketClientProxyImpl(Session session) {
        this.session = session;
    }

    @Override
    public void send(byte[] results) {
        this.session.getAsyncRemote().sendBinary(ByteBuffer.wrap(results));
    }
}
