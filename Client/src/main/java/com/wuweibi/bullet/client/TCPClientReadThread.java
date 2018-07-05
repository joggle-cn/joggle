package com.wuweibi.bullet.client;/**
 * Created by marker on 2018/7/2.
 */

import com.wuweibi.bullet.protocol.MsgProxyHttp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author marker
 * @create 2018-07-02 22:11
 **/
public class TCPClientReadThread extends Thread {


    private Selector selector;

    private WebSocketClientProxy client;

    MsgProxyHttp msg;


    public TCPClientReadThread(MsgProxyHttp msg, WebSocketClientProxy client, Selector selector){
        this.selector = selector;
        this.client = client;
        this.msg = msg;
    }

    @Override
    public void run() {

        try {
            while (selector.select() > 0) {//select()方法只能使用一次，用了之后就会自动删除,每个连接到服务器的选择器都是独立的
                // 遍历每个有可用IO操作Channel对应的SelectionKey
                for (SelectionKey sk : selector.selectedKeys()) {
                    // 如果该SelectionKey对应的Channel中有可读的数据
                    if (sk.isReadable()) {
                        // 使用NIO读取Channel中的数据
                        SocketChannel sc = (SocketChannel) sk.channel();//获取通道信息
                        ByteBuffer buffer = ByteBuffer.allocate(1024);//分配缓冲区大小
                        sc.read(buffer);//读取通道里面的数据放在缓冲区内
                        buffer.flip();// 调用此方法为一系列通道写入或相对获取 操作做好准备
                        // 将字节转化为为UTF-16的字符串
//                        String receivedString = Charset.forName("UTF-16")
//                                .newDecoder().decode(buffer).toString();



                        // 控制台打印出来
                        System.out.println("接收到来自服务器"
                                + sc.socket().getRemoteSocketAddress() + "的信息:"
                                +  "");

                        System.out.println(new String(buffer.array(),"ISO-8859-1"));



                        msg.setContent(buffer.array());
//

                        ByteArrayOutputStream os = new ByteArrayOutputStream();

                        msg.write(os);

                        byte[] results = os.toByteArray();

                        client.send(results);

                        // 为下一次读取作准备
                        sk.interestOps(SelectionKey.OP_READ);
                    }
                    // 删除正在处理的SelectionKey
                    selector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
