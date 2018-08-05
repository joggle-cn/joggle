package com.wuweibi.bullet.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

;

/**
 * 处理服务端 channel.
 */
public class UserChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private Logger logger = LoggerFactory.getLogger(UserChannelHandler.class);

    private static AtomicLong userIdProducer = new AtomicLong(0);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        // 当出现异常就关闭连接
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {

        System.out.println("channelRead0");




        Channel proxyChannel = TcpApplication.cache.get("a");
        if (proxyChannel == null) {

            // 该端口还没有代理客户端
//            ctx.channel().close();
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);

            System.out.println("S-R:" + new String(bytes));

            proxyChannel.writeAndFlush(bytes);
        }


    }




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel serverChannel = ctx.channel();
        InetSocketAddress sa = (InetSocketAddress) serverChannel.localAddress();

        System.out.println("channelActive"+ sa);




        new Thread(){
            @Override
            public void run() {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap
                        .option(ChannelOption.AUTO_READ, true)
                        .option(ChannelOption.SO_KEEPALIVE, true);

//                NioEventLoopGroup boss = new NioEventLoopGroup(1);// 通过nio方式来接收连接和处理连接
                NioEventLoopGroup work = new NioEventLoopGroup(2 * Runtime.getRuntime().availableProcessors());
                bootstrap.group(work);
                bootstrap.channel(NioSocketChannel.class);

                bootstrap.handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
//                        ch.pipeline().addLast(new IdleStateHandler(0, 0, 5));
//                        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(81920, Delimiters.lineDelimiter()));
//                        ch.pipeline().addLast("decoder", new StringDecoder());
//                        ch.pipeline().addLast("encoder", new StringEncoder());

                        ch.pipeline().addLast(new RealServerChannelHandler(serverChannel));
                    }
                });

                bootstrap.connect("192.168.1.4", 22)
                        .addListener(new ChannelFutureListener() {

                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {

                                // 连接后端服务器成功
                                if (future.isSuccess()) {
                                    final Channel realServerChannel = future.channel();
                                    logger.debug("connect realserver success, {}", realServerChannel);

                                    TcpApplication.cache.put("a", realServerChannel);


                                }
                            }
                        });






            }
        }.start();



        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // 通知代理客户端
        super.channelInactive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

        // 通知代理客户端

        super.channelWritabilityChanged(ctx);
    }

    /**
     * 为用户连接产生ID
     *
     * @return
     */
    private static String newUserId() {
        return String.valueOf(userIdProducer.incrementAndGet());
    }
}