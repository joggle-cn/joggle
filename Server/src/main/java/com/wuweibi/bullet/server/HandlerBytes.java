package com.wuweibi.bullet.server;
/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.Sequence;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import io.netty.channel.ChannelHandlerContext;

import javax.websocket.Session;
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


    public static Map<Long, ChannelHandlerContext> cache = new HashMap<>();

    private static final Sequence sequence = new Sequence();


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
        long time = sequence.next();

        cache.put(time, ctx);

        try{


            byte[] bytes = result;
            // 根据域名判断应该请求哪个客户端

            byte[] timebytes = ByteUtils.long2Bytes(time);
            byte[] results = ByteUtils.byteMerger(timebytes, bytes);



            String uuid = "12345678";


            synchronized (HandlerBytes.class){
                for (BulletAnnotation client : BulletAnnotation.connections) {
                    // 获取对应设备Id的链接
                    if(client.getDeviceId().equals(uuid)){
                        ByteBuffer buf = ByteBuffer.wrap(results);
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