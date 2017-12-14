/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.SocketUtils;
import com.wuweibi.bullet.client.SocketThread;
import com.wuweibi.bullet.client.WebSocketClientProxyImpl;
import com.wuweibi.bullet.client.WebSocketClientProxyTest;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.ws.rs.client.Client;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * 本地测试
 * 多线程的
 *
 * @author marker
 * @create 2017-11-19 下午4:19
 **/
public class ClientThredTest extends Draft_6455 {

    private Logger logger = LoggerFactory.getLogger(SocketThread.class);


    @Test
    public void test() throws URISyntaxException, InterruptedException {


        new Thread(){
            @Override
            public void run() {

                ServerBootstrap bootstrap = new ServerBootstrap();
                NioEventLoopGroup boss = new NioEventLoopGroup(1);
                NioEventLoopGroup work = new NioEventLoopGroup(2 * Runtime.getRuntime().availableProcessors());
                bootstrap.group(boss, work);
                bootstrap.channel(NioServerSocketChannel.class);
                bootstrap.localAddress("localhost", 8081);
                bootstrap.childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {

                        // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                        ch.pipeline().addLast(new ByteArrayDecoder());
                        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println("Server 接收到请求数据");
                                byte[] result1 = ( byte[]) msg;

                                // 新增时间
                                int port = 8080;
                                MsgProxyHttp msgProxyHttp = new MsgProxyHttp("localHost", port);
                                logger.info("{}",msgProxyHttp.getHead().toString());
                                msgProxyHttp.setContent(result1);

                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                msgProxyHttp.write(outputStream);
                                // 包装了Bullet协议的
                                byte[] results = outputStream.toByteArray();


                                SocketThread socketThread = new SocketThread(new WebSocketClientProxyTest(ctx), results);

                                socketThread.start();
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
                        });

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
        Thread.sleep(1000000L);


    }






}
