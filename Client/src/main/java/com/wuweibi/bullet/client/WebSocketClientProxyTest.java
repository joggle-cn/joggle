package com.wuweibi.bullet.client;

/**
 * Created by marker on 2017/11/20.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author marker
 * @create 2017-11-20 下午1:12
 **/
public class WebSocketClientProxyTest implements WebSocketClientProxy {

    /** socket */
    private ChannelHandlerContext ctx;



    public WebSocketClientProxyTest(ChannelHandlerContext ctx) {
        this.ctx = ctx;

    }

    @Override
    public void send(byte[] bytes) {
        System.out.println("==============================");

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        MsgProxyHttp msg = null;
        try {
            head.read(bis);//读取消息头
            switch (head.getCommand()) {
                case Message.Proxy_Http:// Bind响应命令
                    msg = new MsgProxyHttp(head);
                    msg.read(bis);
                    ;break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] responseData =  msg.getContent();
        System.out.println("wlen=" + responseData.length);

        if(ctx != null){
            // 在当前场景下，发送的数据必须转换成ByteBuf数组
            try{
                ByteBuf encoded = ctx.alloc().buffer(responseData.length);
                encoded.writeBytes(responseData);
                ctx.writeAndFlush(encoded);
            } catch (Exception e){

            } finally {
                ctx.close();

            }

        }


    }
}
