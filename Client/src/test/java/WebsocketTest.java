/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.client.SocketThread;
import com.wuweibi.bullet.client.WebSocketClientProxyImpl;
import org.apache.commons.io.IOUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.ws.rs.client.Client;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 *
 *
 * @author marker
 * @create 2017-11-19 下午4:19
 **/
public class WebsocketTest extends Draft_6455 {


    @Test
    public void test() throws URISyntaxException, InterruptedException {

        String url = "ws://localhost:8082/ws/chat/marker";


        final WebSocketClient client = new WebSocketClient(new URI(url)) {

            @Override
            public void onOpen(ServerHandshake arg0) {
                System.out.println("打开链接");
            }

            @Override
            public void onMessage(String arg0) {
                System.out.println("收到消息"+arg0);
            }

            @Override
            public void onError(Exception arg0) {
                arg0.printStackTrace();
                System.out.println("发生错误已关闭");
            }

            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
                System.out.println(arg1);
                System.out.println("链接已关闭");
            }

            @Override
            public void onMessage(ByteBuffer message) {
                // 接收到数据发送代理请求，将响应数据放到websocket
                byte[] bytes = message.array();
                new SocketThread(new WebSocketClientProxyImpl(this), bytes).start();
            }


        };

        client.connect();

        while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
            System.out.println("还没有打开");
            Thread.sleep(1000000L);
        }
        System.out.println("打开了");


        new Thread(){
            @Override
            public void run() {
                client.sendPing();
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();



        Thread.sleep(100000L);


    }


    @Test
    public void test1() throws IOException, URISyntaxException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");
        String uri = "ws://localhost:8080/ws/chat/dsadsa";
        Session session = container.connectToServer(Client.class, new URI(uri)); // 连接会话
        session.getBasicRemote().sendText("123132132131"); // 发送文本消息

    }





}
