package com.wuweibi.bullet.server;
/**
 * Created by marker on 2017/12/5.
 */



import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            if (msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) msg;
                HandlerBytes handler = new HandlerBytes(request, ctx);
                handler.run();
            }
        } finally {
//            ReferenceCountUtil.release(msg);
        }



//        System.out.println("==============Simpler=============");
//        System.out.println(result);
//        System.out.println(new String(result));
//        System.out.println("==============Simpler=============");
//
//        System.out.println("=====--=-"+result[result.length -1]);
//        System.out.println("=====--=-"+result[result.length -2]);
//        if(!(result[result.length -1] == '\n' && result[result.length -2] == '\r')){
//            ctx.fireChannelRead(msg);
//            return;
//        }

//        HandlerBytes handler = new HandlerBytes(ctx, result);
//        handler.run();

    }


    /**
     * channelRead执行后触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {






        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER); //flush掉所有写回的数据
//                .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        logger.error("", cause);
        ctx.close();
    }




}