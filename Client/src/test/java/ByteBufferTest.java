/**
 * Created by marker on 2017/11/19.
 */

import org.apache.commons.io.IOUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.ws.rs.client.Client;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 *
 *
 * @author marker
 * @create 2017-11-19 下午4:19
 **/
public class ByteBufferTest extends Draft_6455 {


    @Test
    public void test() throws URISyntaxException, InterruptedException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);


        byte[] bytes = new byte[]{
                1,3
        };

        byteBuffer.putInt(1);
        byteBuffer.put(bytes);



        byteBuffer.flip();


        System.out.println(byteBuffer.getInt());

        byte[] bytes2 = new byte[2];

        System.out.println(byteBuffer.get(bytes2));


        System.out.println(bytes2[0]+ " " +bytes2[1]);

    }



}
