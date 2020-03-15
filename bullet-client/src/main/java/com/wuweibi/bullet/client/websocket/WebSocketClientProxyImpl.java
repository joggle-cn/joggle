package com.wuweibi.bullet.client.websocket;
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
    public void send(byte[] results, boolean b) {
        try {
            this.session.getBasicRemote().sendBinary(ByteBuffer.wrap(results),b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
