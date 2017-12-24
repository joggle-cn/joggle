/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ConfigUtils;
import com.wuweibi.bullet.client.Client;
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
public class Websocket2Test  {


    @Test
    public void test() throws URISyntaxException, InterruptedException, IOException, DeploymentException {

        String url = ConfigUtils.getTunnel() +"/"+ ConfigUtils.getDeviceId();
        System.out.println(url);


        WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");

        Session session1 = container.connectToServer(Client.class, new URI(url)); // 连接会话


        Thread.sleep(1000000L);





    }








}
