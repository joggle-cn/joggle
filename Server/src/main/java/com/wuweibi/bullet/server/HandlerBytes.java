package com.wuweibi.bullet.server;
/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.dto.DeviceMappingDto;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.StringHttpUtils;
import com.wuweibi.bullet.utils.StringUtils;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
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
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(HandlerBytes.class);

    /** 寄存存响应对象 */
    public static Map<String, ChannelHandlerContext> cache = Collections.synchronizedMap(new HashMap<>());


    /**
     * 用于响应
     */
    private ChannelHandlerContext ctx;

    /** 请求内容 */
    private byte[] result;

    private HttpRequest request;


    public HandlerBytes(HttpRequest request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;

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
        request.headers().set(HttpHeaderNames.CONNECTION, "close");// 不支持长链接
        try {
            httpRequestStr = request.toString() + "\n\r\n";
            httpRequestStr = httpRequestStr.substring(61);
            logger.debug("======================\n{}", httpRequestStr);
            this.result = httpRequestStr.getBytes();

            // 获取请求的host
            String domainHost = request.headers().get(HttpHeaderNames.HOST);

            // 获取二级域名
            String sldomain = StringHttpUtils.getSecondLevelDomain(domainHost);


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
            String host = mapping.getHost();

            String localHost = "localhost";
            if(!StringUtils.isEmpty(host)){
                localHost = host;
            }


            msgProxyHttp = new MsgProxyHttp(localHost, port);

            logger.debug("sequence={}", msgProxyHttp.getSequence());

            logger.info("{}",msgProxyHttp.getHead().toString());
            msgProxyHttp.setContent(result);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            msgProxyHttp.write(outputStream);
            // 包装了Bullet协议的
            resultBytes = outputStream.toByteArray();

        } catch (Exception e) {
            logger.error("req:"+new String(result), e);
        }
        // 生成的序号，以便在响应回来的时候找到对应的响应对象
        String key = "";


        try{
            key = msgProxyHttp.getSequence();
            // TODO 使用相关算法获取多个连接中的一个
            CoonPool pool = SpringUtils.getBean(CoonPool.class);
            BulletAnnotation client = pool.getByDeviceNo(deviceCode);

            Session session = client.getSession();

            ByteBuffer buf = ByteBuffer.wrap(resultBytes);
//            System.out.println("==============================" + session.isOpen());

            synchronized (session.getId()){
                if (session.isOpen()){
                    session.getBasicRemote().sendBinary(buf);
                    return;
                }
            }
        } catch (Exception e){
            logger.error("", e);
        } finally {
            cache.put(key, ctx);
        }

        // 设备没有上线
        if(ctx != null){
            sendMessage("sorry! ChannelHandlerContext is null!");
        }
        cache.remove(key);



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