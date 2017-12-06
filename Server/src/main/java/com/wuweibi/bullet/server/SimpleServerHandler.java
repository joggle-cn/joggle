package com.wuweibi.bullet.server;/**
 * Created by marker on 2017/12/5.
 */

/**
 * @author marker
 * @create 2017-12-05 下午10:00
 **/

    import io.netty.channel.ChannelHandlerContext;
    import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] result1 = ( byte[]) msg;

        // 解析http协议，获取域名

        // 通过域名找到设备ID与映射端口




        Thread workThread = new Thread(new HandlerBytes(ctx, result1));    //创建线程
        workThread.start();                                    //启动线程

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}