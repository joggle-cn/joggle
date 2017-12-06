package com.wuweibi.bullet.client;/**
 * Created by marker on 2017/11/20.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.SocketUtils;
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

    /** host */
    private String host;

    /** 端口 */
    private int port;


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

        byte[] timeBytes =  new byte[8];
        System.arraycopy(bytes, 0, timeBytes, 0, 8);

        logger.debug("time = {}", ByteUtils.bytes2Long(timeBytes));
        int size = bytes.length - 8;
        logger.debug("size = {}", size);
        byte[] requestData = new byte[size];
        System.arraycopy(bytes, 8, requestData, 0, size);

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
            logger.error("", e);
        }
        // 处理响应
            logger.debug("接收响应内容...");

            byte[] bytesout = SocketUtils.receiveData(socketChannel);


            byte[] results  = ByteUtils.byteMerger(timeBytes, bytesout);
            client.send(results);

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
