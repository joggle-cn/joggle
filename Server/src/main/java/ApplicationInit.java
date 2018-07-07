/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.server.SimpleServerHandler;
import com.wuweibi.bullet.utils.SpringUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author marker
 * @create 2017-11-19 下午5:44
 **/
public class ApplicationInit  implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ApplicationInit.class);



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


        int port = SpringUtils.getPropInt("bullet.server.port");

        logger.info("===========================================================");
        logger.info("Bullet Server Port={}", port);
        logger.info("===========================================================");



        new Thread(() -> {

            ServerBootstrap bootstrap = new ServerBootstrap();// 引导辅助程序
            NioEventLoopGroup boss = new NioEventLoopGroup(1);// 通过nio方式来接收连接和处理连接
            NioEventLoopGroup work = new NioEventLoopGroup(2 * Runtime.getRuntime().availableProcessors());
            bootstrap.group(boss, work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));

//            bootstrap.localAddress(port);

            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
//                ch.pipeline().addLast(new ByteArrayDecoder());
                    ch.pipeline().addLast(new HttpRequestDecoder());
//                    .addLast();
                    // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                ch.pipeline()
                        .addLast(new SimpleServerHandler());

                }
            }).option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .childOption(ChannelOption.SO_KEEPALIVE, false);

            try {
                // 开始绑定server,阻塞直到绑定成功
                ChannelFuture channelFuture = bootstrap.bind(port).sync();

                logger.info(">server started");

                //阻塞直到关闭成功
                channelFuture.channel().closeFuture().sync();

                logger.info(">server close");
            } catch (InterruptedException e) {
                logger.error("", e);
            } finally {
                // 关闭资源,boss线程组及work线程组
                boss.shutdownGracefully();
                work.shutdownGracefully();
            }

        }).start();





    }
}
