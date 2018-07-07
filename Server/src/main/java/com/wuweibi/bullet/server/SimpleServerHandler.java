package com.wuweibi.bullet.server;
/**
 * Created by marker on 2017/12/5.
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
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


/**
 *
 * SimpleServerHandler 处理请求
 *
 * @author marker
 * @create 2017-12-05 下午10:00
 **/
@ChannelHandler.Sharable//注解@Sharable可以让它在channels间共享
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(SimpleServerHandler.class);



    /**
     * 用于响应
     */
    private ChannelHandlerContext ctx;

    /** 请求内容 */
    private byte[] result;

    private HttpRequest request;


    // 请求数据存放
    ByteArrayOutputStream requestStream = new ByteArrayOutputStream();




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ctx = ctx;
        try {
            if (msg instanceof HttpRequest) {
                this.request = (HttpRequest) msg;

                // 篡改请求
                request.headers().set(HttpHeaderNames.CONNECTION, "close");// 不支持长链接

                String httpRequestStr = request.toString() + "\n\r\n";
                httpRequestStr = httpRequestStr.substring(61);
                logger.debug("==========httpRequestStr============\n{}", httpRequestStr);
                requestStream.write( httpRequestStr.getBytes());

            } else if(msg instanceof DefaultHttpContent){
                DefaultHttpContent lastHttpContent = (DefaultHttpContent) msg;

                ByteBuf in = lastHttpContent.content();

                in.markReaderIndex();                  //我们标记一下当前的readIndex的位置

                int len = in.readableBytes();

//                if (len < requestLength) { //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
//                    in.resetReaderIndex();
//                    return;
//                }

                byte[] body = new byte[len];  //  嗯，这时候，我们读到的长度，满足我们的要求了，把传送过来的数据，取出来吧~~
                in.readBytes(body);  //
                requestStream.write(body);

            } else if(msg instanceof DefaultLastHttpContent){

                DefaultLastHttpContent lastHttpContent = (DefaultLastHttpContent) msg;

                ByteBuf in = lastHttpContent.content();


                int requestLength = request.headers().getInt(HttpHeaderNames.CONTENT_LENGTH);

                in.markReaderIndex();                  //我们标记一下当前的readIndex的位置

                int len = in.readableBytes();

//                if (len < requestLength) { //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
//                    in.resetReaderIndex();
//                    return;
//                }

                byte[] body = new byte[len];  //  嗯，这时候，我们读到的长度，满足我们的要求了，把传送过来的数据，取出来吧~~
                in.readBytes(body);  //
                requestStream.write(body);
            }
        } finally {
//            ReferenceCountUtil.release(msg);
        }

    }


    /**
     * channelRead执行后触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        if(request == null)
            return;


        String deviceCode = "";
        // 解析http协议，获取域名
        byte[] resultBytes = new byte[0];
        MsgProxyHttp msgProxyHttp = null;
        try {

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

            logger.info("{}", msgProxyHttp.getHead().toString());
            msgProxyHttp.setContent(requestStream.toByteArray());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            msgProxyHttp.write(outputStream);
            // 包装了Bullet协议的
            resultBytes = outputStream.toByteArray();

        } catch (Exception e) {
            logger.error("req:"+new String(result == null?new byte[]{}: result), e);
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

            synchronized (session.getId()){
                if (session.isOpen()){
                    session.getBasicRemote().sendBinary(buf);
                    return;
                }
            }
        } catch (Exception e){
            logger.error("", e);
        } finally {
            HandlerBytes.cache.put(key, ctx);
        }

        // 设备没有上线
        if(ctx != null){
            sendMessage("sorry! your device not online！");
        }
        HandlerBytes.cache.remove(key);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        logger.error("", cause);
        ctx.close();
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

        ctx.writeAndFlush(encoded) //flush掉所有写回的数据
                .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
    }


}