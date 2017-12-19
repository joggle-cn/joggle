package com.wuweibi.bullet.client;/**
 * Created by marker on 2017/11/20.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.SocketUtils;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import io.netty.buffer.ByteBuf;
import org.apache.commons.io.IOUtils;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author marker
 * @create 2017-11-20 下午1:02
 **/
public class SocketThread extends Thread{
    private Logger logger = LoggerFactory.getLogger(SocketThread.class);

    /** 客户端代理 */
    private WebSocketClientProxy client;

    /** bytes */
    private byte[] bytes;



    /**
     * 构造
     * @param client 代理客户端
     * @param bytes 数据
     */
    public SocketThread(WebSocketClientProxy client, byte[] bytes) {
        this.client = client;
        this.bytes = bytes;

    }

    @Override
    public void run() {
        logger.debug("接收到服务器的转发请求信息！");

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        MsgProxyHttp msg = null;
        try {
            head.read(bis);//读取消息头
            logger.info("{}", head.toString());

            switch (head.getCommand()) {
                case Message.Proxy_Http:// Bind响应命令
                    msg = new MsgProxyHttp(head);
                    msg.read(bis);
                break;
            }
        } catch (IOException e) {
            logger.error("", e);
        }


        String host = msg.getServerAddr();
        int  port   = msg.getPort();
        byte[] requestData = msg.getContent();

        SocketChannel socketChannel = null;
        // 代理请求
        try {
            Selector selector = Selector.open();
            InetSocketAddress isa = new InetSocketAddress(host, port);
            socketChannel = SocketChannel.open(isa);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            ByteBuffer buffer = ByteBuffer.wrap(requestData);
            socketChannel.write(buffer);
            socketChannel.shutdownOutput();

        } catch (IOException e) {
//            logger.error("", e);
            msg.setContent(getMessage(e.getMessage()));
        }
        // 处理响应
        logger.debug("接收响应内容...");
        byte[] bytesout = new byte[0];
        try {
            bytesout = SocketUtils.receiveData(socketChannel);
            msg.setContent(bytesout);
        } catch (IOException e) {
            msg.setContent(getMessage(e.getMessage()));

        } catch (NullPointerException e) {
            msg.setContent(getMessage("server "+msg.getServerAddr()+":"+msg.getPort()+" invoke faild!"));
        }
        System.out.println("wlen=" + bytesout.length);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            msg.write(os);
        } catch (IOException e) {
            logger.error("", e);
        }

        byte[] results = os.toByteArray();

        client.send(results);

    }



    /**
     * 发送消息
     *
     * @param msg 消息内容
     */
    private byte[] getMessage(String msg){
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n");
        sb.append("Content-Type:text/html; charset:GBK");
        sb.append("\n\n");
        sb.append(msg);
        return sb.toString().getBytes();
    }

}
