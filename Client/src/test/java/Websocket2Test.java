/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ConfigUtils;
import com.wuweibi.bullet.client.Client;
import com.wuweibi.bullet.client.SocketThread;
import com.wuweibi.bullet.client.WebSocketClientProxyImpl;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 *
 *
 * @author marker
 * @create 2017-11-19 下午4:19
 **/
public class Websocket2Test extends Draft_6455 {


    @Test
    public void test() throws URISyntaxException, InterruptedException, IOException, DeploymentException {


        String uuid = "12345678";


        String url = ConfigUtils.getTunnel() +"/"+ uuid;
        System.out.println(url);


        WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");

        Session session1 = container.connectToServer(Client.class, new URI(url)); // 连接会话







        Thread.sleep(1000000L);





    }








}
