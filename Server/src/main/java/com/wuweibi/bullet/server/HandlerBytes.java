package com.wuweibi.bullet.server;
/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.Sequence;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.dto.DeviceMappingDto;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.StringHttpUtils;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import io.netty.buffer.ByteBuf;
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

        String deviceCode = "";

        // 解析http协议，获取域名
        String httpRequestStr = null;
        byte[] resultBytes = new byte[0];
        MsgProxyHttp msgProxyHttp = null;
        try {
            httpRequestStr = new String(result,"utf-8");

            String host = StringHttpUtils.getHost(httpRequestStr);

            // 获取二级域名
            String sldomain = StringHttpUtils.getSecondLevelDomain(host);


            // 通过域名找到设备ID与映射端口

            DeviceMappingService deviceMappingService = SpringUtils.getBean(DeviceMappingService.class);

            DeviceMappingDto mapping = deviceMappingService.getMapping(sldomain);
            if(mapping == null){
                sendMessage("sorry! device is not mapping info!");
                return;
            }

            // 设备编码
            deviceCode = mapping.getDeviceCode();
            int port = mapping.getPort();
            String localHost = "localhost";

            msgProxyHttp = new MsgProxyHttp(localHost, port);
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


        try{
            synchronized (HandlerBytes.class){
                // TODO 使用相关算法获取多个连接中的一个
                CoonPool pool = SpringUtils.getBean(CoonPool.class);

                BulletAnnotation client = pool.getByDeviceNo(deviceCode);

                ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                Session session = client.getSession();

                if (session.isOpen()){
                    cache.put(seq, ctx);
                    session.getBasicRemote().sendBinary(buf,true);
                    return;
                }
            }
        } catch (Exception e){
            logger.error("", e);
        }

        // 设备没有上线
        if(ctx != null){
            sendMessage("sorry! device is not online!");
        }




    }


    /**
     * 发送消息
     *
     * @param msg 消息内容
     */
    private void sendMessage(String msg){
        // 在当前场景下，发送的数据必须转换成ByteBuf数组
        ByteBuf encoded = ctx.alloc().buffer(1024);
        encoded.writeBytes("HTTP/1.1 200 OK\n".getBytes());
        encoded.writeBytes("Content-Type:text/html; charset:GBK".getBytes());
        encoded.writeBytes("\n\n".getBytes());
        encoded.writeBytes(msg.getBytes());

        ctx.write(encoded);
        ctx.flush();
        ctx.close();
    }


}