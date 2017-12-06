/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.SocketUtils;
import com.wuweibi.bullet.client.SocketThread;
import com.wuweibi.bullet.client.WebSocketClientProxyImpl;
import com.wuweibi.bullet.client.WebSocketClientProxyTest;
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

                try {
                    ServerSocket serverSocket = new ServerSocket(8081);

                    while(true){
                        Socket socket = null;
                        try{
                            socket = serverSocket.accept();                        //主线程获取客户端连接
                            System.out.println("Server 接收到请求数据");

                            byte[] bytes = SocketUtils.getInputStreamBytes(socket);



                            // 新增时间
                            long time = System.currentTimeMillis();
                            byte[] timebytes = ByteUtils.long2Bytes(time);
                            byte[] results = ByteUtils.byteMerger(timebytes, bytes);


                            SocketThread socketThread = new SocketThread(new WebSocketClientProxyTest(socket), results);
                            socketThread.setHost("localhost");
                            socketThread.setPort(8080);
                            socketThread.start();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
        Thread.sleep(1000000L);


    }






}
