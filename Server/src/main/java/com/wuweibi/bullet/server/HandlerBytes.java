package com.wuweibi.bullet.server;
/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.Sequence;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import com.wuweibi.bullet.utils.StringHttpUtils;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * 服务器接收到请求，准备发送代理请求
 *
 * @author marker
 * @create 2017-11-19 下午5:47
 **/
public class HandlerBytes implements Runnable{

    private Logger logger = LoggerFactory.getLogger(HandlerBytes.class);

    /** 寄存存响应对象 */
    public static Map<String, ChannelHandlerContext> cache = new HashMap<>();


    /**
     * 用于响应
     */
    private ChannelHandlerContext ctx;

    /** 请求内容 */
    private byte[] result;

    public HandlerBytes(ChannelHandlerContext ctx, byte[] result1) {
        this.ctx = ctx;
        this.result = result1;
    }


    /**
     * 线程
     */
    public void run(){


        // 解析http协议，获取域名
        String httpRequestStr = null;
        byte[] resultBytes = new byte[0];
        MsgProxyHttp msgProxyHttp = null;
        try {
            httpRequestStr = new String(result,"utf-8");

            String host = StringHttpUtils.getHost(httpRequestStr);

            // 通过域名找到设备ID与映射端口
            msgProxyHttp = new MsgProxyHttp(host, 8080);
            logger.info("{}",msgProxyHttp.getHead().toString());
            msgProxyHttp.setContent(result);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            msgProxyHttp.write(outputStream);
            // 包装了Bullet协议的
            resultBytes = outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String seq = msgProxyHttp.getSequence();

        cache.put(seq, ctx);

        try{
            String uuid = "12345678";


            synchronized (HandlerBytes.class){
                for (BulletAnnotation client : BulletAnnotation.connections) {
                    // 获取对应设备Id的链接
                    if(client.getDeviceId().equals(uuid)){

                        ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                        Session session = client.getSession();

                        if(session.isOpen()){
                            session.getBasicRemote().sendBinary(buf,true);

                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }




}