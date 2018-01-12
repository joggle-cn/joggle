package com.wuweibi.bullet.server;
/**
 * Created by marker on 2017/12/5.
 */



import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * SimpleServerHandler 处理请求
 *
 * @author marker
 * @create 2017-12-05 下午10:00
 **/
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(SimpleServerHandler.class);



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] result = ( byte[]) msg;

        Thread workThread = new Thread(new HandlerBytes(ctx, result));    //创建线程
        workThread.start();                                    //启动线程

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        logger.error("", cause);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}