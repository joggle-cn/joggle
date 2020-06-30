package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2017/11/20.
 */

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.client.service.CommandThreadPool;
import com.wuweibi.bullet.client.service.SpringUtil;
import com.wuweibi.bullet.client.utils.ConfigUtils;
import com.wuweibi.bullet.client.websocket.WebSocketClientProxy;
import com.wuweibi.bullet.protocol.*;
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
        CommandThreadPool pool = SpringUtil.getBean(CommandThreadPool.class);

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        try {
            head.read(bis);//读取消息头

            switch (head.getCommand()) {

                case Message.DEVICE_NO: // 分配UUID
                    MsgDeviceNo msgDeviceNo = new MsgDeviceNo(head);
                    msgDeviceNo.read(bis);

                    String deviceNo = msgDeviceNo.getDeviceNo();
                    logger.error("================================================");
                    logger.error("================ Device NO =====================");
                    logger.error("=== {} ===", deviceNo);
                    logger.error("================================================");

                    ConfigUtils.setDeviceNo(deviceNo);
                    ConfigUtils.store();

                    break;
                case Message.NEW_UNMAPPING: // HTTP代理请求
                    MsgUnMapping msg = new MsgUnMapping(head);
                    msg.read(bis);
                    String json = msg.getJson();
                    MappingInfo info = JSON.parseObject(json, MappingInfo.class);
                    Long mpId = info.getId();
                    pool.killThread(mpId);

                    break;
                case Message.DEVICE_SECRET: //
                    MsgDeviceSecret msgDeviceSecret = new MsgDeviceSecret(head);
                    msgDeviceSecret.read(bis);
                    String secret = msgDeviceSecret.getSecret();
                    new UpdateSecretThread(secret).start();
                    break;

                case Message.NEW_MAPPING: // 新的映射请求


                    MsgMapping msg2 = new MsgMapping(head);
                    msg2.read(bis);

//                    System.out.println(msg2.getJson());
                    //   {"protocol":1,"port":8080,"domain":"test","host":"192.168.1.4","description":"GitLab","id":16,"deviceId":6,"userId":1}


                    MappingInfo mappingInfo = JSON.parseObject(msg2.getJson(), MappingInfo.class);



                    // 判断服务是否正在运转
                    Long mappingId = mappingInfo.getId();

                    if(!pool.containsId(mappingId)){
                        pool.push( new CommandThread(mappingInfo));
                    } else {
                        pool.killThread(mappingId);

                        pool.push( new CommandThread(mappingInfo));
                    }
                    break;
                case Message.Heart:// 心跳消息
                    logger.debug("resave pong heard");

                    break;
                case Message.LOG_MAPPING_STATUS:// 日志开关
                    MsgLogOpen msg3 = new MsgLogOpen(head);
                    msg3.read(bis);

                    Long mappin2gId = msg3.getMappingId();
                    CommandThreadPool commandThreadPool = SpringUtil.getBean(CommandThreadPool.class);
                    CommandThread commandThread = commandThreadPool.getThread(mappin2gId);

                    if(msg3.getOpen() == 1){

                       commandThread.openLog();

                    }else{
                        commandThread.closeLog();
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
