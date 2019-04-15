package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2017/11/20.
 */

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.client.websocket.WebSocketClientProxy;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.client.service.CommandThreadPool;
import com.wuweibi.bullet.client.service.SpringUtil;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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

                    break;

                case Message.NEW_MAPPING: // 新的映射请求

                    logger.debug("新的映射请求。。");

                    MsgMapping  msg2 = new MsgMapping(head);
                    msg2.read(bis);

                    System.out.println(msg2.getJson());
                    //   {"protocol":1,"port":8080,"domain":"test","host":"192.168.1.4","description":"GitLab","id":16,"deviceId":6,"userId":1}


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
