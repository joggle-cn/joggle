package com.wuweibi.bullet.client;/**
 * Created by marker on 2017/11/20.
 */

import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 *
 * 接收到请求
 *
 * @author marker
 * @create 2017-11-20 下午1:02
 **/
public class SocketThread extends Thread {

    /** 日志 */
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
            logger.debug("{}", head.toString());

            switch (head.getCommand()) {
                case Message.Proxy_Http: // HTTP代理请求
                    msg = new MsgProxyHttp(head);
                    msg.read(bis);
                break;
            }
        } catch (IOException e) {
            logger.error("", e);
        }


        String host = msg.getServerAddr();
        int    port = msg.getPort();
        byte[] requestData = msg.getContent();
        logger.debug("{}", new String(requestData));


        Socket socket = new Socket();




        // 代理请求
        try {


            socket.connect(new InetSocketAddress(host, port));
            OutputStream writer = socket.getOutputStream();
            writer.write(requestData);
            writer.flush();
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
//            socket.shutdownOutput();

            InputStream inputStream = socket.getInputStream();


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int headLength = 0;

            String line = null;

            int contentLength = 0;
            do {
                int tmp = -1;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                boolean hasData = false;
                while ((tmp = inputStream.read()) != -1){
                    headLength++;
                    if(tmp == '\n'){
                        hasData = true;
                        break;
                    }
                    os.write((byte)tmp);
                }
                line = new String(os.toByteArray());
                if (line.startsWith("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                if(line.startsWith("Content-Encoding")){

                }

                if (line.startsWith("Connection")) {
                    line = "Connection : close";
                }
                outputStream.write((line + "\n").getBytes());

                System.out.println(line);
                if("\r".equals(line)){
                    outputStream.write("\r".getBytes());
                    break;
                }


                if(!hasData){ // 没有读取到数据
                    break;
                }
            } while (line != null);

            // 读取body阶段
            logger.debug("head长度为:{}", headLength);
            logger.debug("body长度为:{}", contentLength);
            logger.debug("total长度为:{}", contentLength + headLength);

            if( contentLength != 0){
                byte[] buffer = new byte[contentLength];

                int nIdx = 0;
                int nReadLen = 0;
                while (nIdx < contentLength)  {
                    nReadLen = inputStream.read(buffer,   nIdx, contentLength - nIdx);
                    if (nReadLen > 0)  {
                        nIdx = nIdx + nReadLen;
                    } else  {
                        break;
                    }
                }
                outputStream.write(buffer);
            } else {// TODO 分块加载
                do {
                    int tmp = -1;
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    boolean hasData = false;
                    while ((tmp = inputStream.read()) != -1) {
                        headLength++;
                        os.write((byte) tmp);
                        if (tmp == 10) {// LF
                            break;
                        }
                    }
                    byte[] lineByte = os.toByteArray();
                    String line1 = new String(lineByte);
                    System.out.print(line1);


                    // 判断是否读取到结束符号
                    byte[] lineTag;
                    if(lineByte.length == 3){
                        lineTag = Arrays.copyOfRange(lineByte, lineByte.length - 3, lineByte.length );

                        byte[] endTag = new byte[]{(int) 48, (int) 13, (int) 10};
                        if (Arrays.equals(lineTag, endTag)) { //

                            outputStream.write((int) 13);
                            outputStream.write((int) 10);
                            break;
                        }
                    }


                    // 判断一行数据
                    if(lineByte.length > 2) {
                        lineTag = Arrays.copyOfRange(lineByte, lineByte.length - 2, lineByte.length);
                        byte[] aa = new byte[]{(int) 13, (int) 10};
                        if (Arrays.equals(lineTag, aa)) { //
                            System.out.println("行结束符");
                            outputStream.write(lineByte);
                        }
                    }
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while ( true );
            }
            msg.setContent(outputStream.toByteArray());



            ByteArrayOutputStream os = new ByteArrayOutputStream();

            msg.write(os);

            byte[] results = os.toByteArray();
            // 592776

            client.send(results);


        } catch (IOException e) {
            msg.setContent(getMessage(e.getMessage()));
        }


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
