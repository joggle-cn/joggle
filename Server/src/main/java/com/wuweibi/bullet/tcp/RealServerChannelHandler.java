package com.wuweibi.bullet.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理服务端 channel.
 */
public class RealServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static Logger logger = LoggerFactory.getLogger(RealServerChannelHandler.class);

    private Channel serverChannel;

    public RealServerChannelHandler(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        Channel realServerChannel = ctx.channel();
        Channel channel = realServerChannel.attr(Constants.NEXT_CHANNEL).get();

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.out.println(new String(bytes));

        if(serverChannel.isWritable()){
            serverChannel.writeAndFlush(bytes);
        }


        logger.debug("write data to proxy server, {}, {}", realServerChannel, channel);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel();


        super.channelInactive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel();
        Channel proxyChannel = realServerChannel.attr(Constants.NEXT_CHANNEL).get();
        if (proxyChannel != null) {
            proxyChannel.config().setOption(ChannelOption.AUTO_READ, realServerChannel.isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }
}