package com.wuweibi.bullet.client;/**
 * Created by marker on 2017/11/20.
 */

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.client.command.CommandThread;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.client.service.CommandThreadPool;
import com.wuweibi.bullet.client.service.SpringUtil;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.MsgMapping;
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

            switch (head.getCommand()) {
                case Message.Proxy_Http: // HTTP代理请求
                    msg = new MsgProxyHttp(head);
                    msg.read(bis);



                    String host = msg.getServerAddr();
                    int    port = msg.getPort();
                    byte[] requestData = msg.getContent();

                    logger.debug("=============== 代理请求头 start====================\n{}" +
                                    "=============== 代理请求头 end====================",
                            new String(requestData));



                    Socket socket = new Socket();




                    // 代理请求
                    try {


                        socket.connect(new InetSocketAddress(host, port));
                        OutputStream writer = socket.getOutputStream();
                        writer.write(requestData);
                        writer.flush();

                        InputStream inputStream = socket.getInputStream();


                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int headLength = 0;

                        boolean TransferEncoding = false;

                        String line = null;

                        int contentLength = 0;
                        do {
                            int tmp = -1;
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            boolean hasData = false;
                            while ((tmp = inputStream.read()) != -1){
                                headLength++;
                                os.write((byte)tmp);
                                if(tmp == '\n'){
                                    hasData = true;
                                    break;
                                }
                            }
                            line = new String(os.toByteArray());
                            if (line.startsWith("Content-Length")) {
                                contentLength = Integer.parseInt(line.split(":")[1].trim());
                            }
                            if(line.startsWith("Content-Encoding")){

                            }
                            if(line.startsWith("Transfer-Encoding: chunked")){
                                TransferEncoding = true;
                            }


                            if (line.startsWith("Connection")) {
                                line = "Connection : close\r\n";
                            }
                            outputStream.write((line).getBytes());
                            if("\r\n".equals(line)){
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
                        } else {
                            if(TransferEncoding){ // 分块加载
                                do {
                                    int tmp = -1;
                                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                                    while ((tmp = inputStream.read()) != -1) {
                                        headLength++;
                                        os.write((byte) tmp);
                                        if (tmp == 10) {// LF
                                            break;
                                        }
                                    }
                                    byte[] lineByte = os.toByteArray();

                                    byte[] lineTag;
                                    // 判断是否读取到结束符号
                                    if(lineByte.length == 3){
                                        lineTag = Arrays.copyOfRange(lineByte, lineByte.length - 3, lineByte.length );
                                        byte[] endTag = new byte[]{(int) 48, (int) 13, (int) 10};
                                        if (Arrays.equals(lineTag, endTag)) { //

                                            outputStream.write(lineTag);

                                            outputStream.write(inputStream.read());
                                            outputStream.write(inputStream.read());
                                            break;
                                        }
                                    }

                                    if(lineByte.length < 2){

                                        outputStream.write(13);
                                        outputStream.write(10);

                                        break;
                                    }
                                    outputStream.write(lineByte);
                                    lineTag = Arrays.copyOfRange(lineByte, 0, lineByte.length - 2);
                                    String line1 = new String(lineTag);
                                    //                    String line1 = new String(lineByte);
                                    //                    System.out.print(line1);

                                    int len = Integer.valueOf(line1, 16);

                                    int totalLen = len + 2;
                                    byte[] buffer = new byte[totalLen];
                                    int nIdx = 0;
                                    int nReadLen = 0;
                                    while (nIdx < totalLen)  {
                                        nReadLen = inputStream.read(buffer,   nIdx, totalLen - nIdx);
                                        if (nReadLen > 0)  {
                                            nIdx = nIdx + nReadLen;
                                        } else  {
                                            break;
                                        }
                                    }
                                    outputStream.write(buffer);





                                } while ( true );
                            } else {

                                byte[] buffer = new byte[1024];
                                int tmp = -1;
                                while ((tmp = inputStream.read(buffer) ) != -1)  {
                                    outputStream.write(Arrays.copyOfRange(buffer, 0, tmp));
                                }
                            }
                        }




                        byte[] data =outputStream.toByteArray();
//            FileOutputStream file = new FileOutputStream("./data.txt");
//            file.write(data);
//            file.flush();
//            file.close();

                        logger.debug("响应得到数据的长度为：{}", data.length);
                        msg.setContent(data);



                        ByteArrayOutputStream os = new ByteArrayOutputStream();

                        msg.write(os);

                        byte[] results = os.toByteArray();
                        // 592776

                        client.send(results);


                    } catch (IOException e) {
                        msg.setContent(getMessage(e.getMessage()));
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }





                    break;

                case Message.NEW_MAPPING: // 新的映射请求

                    logger.debug("新的映射请求。。");

                    MsgMapping  msg2 = new MsgMapping(head);
                    msg2.read(bis);

                    System.out.println(msg2.getJson());
                    //   {"protocol":1,"port":8080,"domain":"test","host":"192.168.1.4","description":"GitLab","id":16,"deviceId":6,"userId":1}

                    msg2.saveFile("./tmp.yml");

                    MappingInfo mappingInfo = JSON.parseObject(msg2.getJson(), MappingInfo.class);



                    CommandThreadPool pool = SpringUtil.getBean(CommandThreadPool.class);

                    // 判断服务是否正在运转
                    Long mappingId = mappingInfo.getId();

                    if(!pool.containsId(mappingId)){
                        pool.push( new CommandThread(mappingInfo));
                    } else {
                        pool.killThread(mappingId);

                        pool.push( new CommandThread(mappingInfo));
                    }
                    break;

            }
        } catch (IOException e) {
            logger.error("", e);
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
