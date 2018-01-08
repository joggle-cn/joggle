/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.server.Handler;
import com.wuweibi.bullet.server.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author marker
 * @create 2017-11-19 下午5:44
 **/
public class ApplicationInit implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(ApplicationInit.class);



//    @Value("#{bullet.server.port}")
    private int port = 8081;



    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("===========================================================");
        logger.info("Bullet Server Port={}", port);
        logger.info("===========================================================");



        new Thread(){
            @Override
            public void run() {

                ServerBootstrap bootstrap = new ServerBootstrap();
                NioEventLoopGroup boss = new NioEventLoopGroup(1);
                NioEventLoopGroup work = new NioEventLoopGroup(2 * Runtime.getRuntime().availableProcessors());
                bootstrap.group(boss, work);
                bootstrap.channel(NioServerSocketChannel.class);
                bootstrap.localAddress("localhost", port);
                bootstrap.childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {

                    // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                    ch.pipeline().addLast(new ByteArrayDecoder());
                    // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                    ch.pipeline().addLast(new SimpleServerHandler());

                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);;

                try {
                    // 开始绑定server,阻塞直到绑定成功
                    ChannelFuture channelFuture = bootstrap.bind().sync();

                    System.out.println(">server started");

                    //阻塞直到关闭成功
                    channelFuture.channel().closeFuture().sync();

                    System.out.println(">server close");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭资源,boss线程组及work线程组
                    boss.shutdownGracefully();
                    work.shutdownGracefully();
                }

            }
        }.start();


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


}
