/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.wuweibi.bullet.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.protocol.*;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.CodeHelper;
import com.wuweibi.bullet.utils.SpringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;


/**
 * WebSocket服务
 *
 *
 * @author marker
 * @version 1.0
 */
@ServerEndpoint(value = "/tunnel/{deviceNo}" )
public class BulletAnnotation {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(BulletAnnotation.class);

    /** session */
    private Session session;

    // 设备状态
    private boolean deviceStatus = false;

    /** ID */
    private int id;

    /**  设备ID  */
    private String deviceNo;


    @Autowired
    private CoonPool pool = SpringUtils.getBean(CoonPool.class);

    public BulletAnnotation() {
    }


    /**
     *
     * 客户端打开连接
     * @param session session
     */
    @OnOpen
    public void open(Session session,
             // 设备编号（服务器端生成)
             @PathParam("deviceNo")String deviceNo) {
        this.session  = session;
        this.deviceNo = deviceNo;

        session.setMaxBinaryMessageBufferSize(101024000);
        session.setMaxIdleTimeout(0);

        if(StringUtils.isBlank(deviceNo) || "null".equals(deviceNo)){ //如果是首次链接
            this.deviceNo = CodeHelper.makeDeviceNo();
        }

        // TODO 校验设备是否被绑定（设备被绑定后同样的名称不在链接成功)



        // 更新设备状态
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
        if(deviceOnlineService.existsOnline(this.deviceNo )){// 设备已经存在线列表里了
            logger.warn("{} 设备已经在线", this.deviceNo);
            // 这里判断的前提是设备被绑定后，不能有其他设备用同样的NO链接
            try {
                this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "在线的设备编号已经被绑定"));
            } catch (IOException e) {
                logger.error("", e);
            }
            return;
        }

        deviceOnlineService.saveOrUpdateOnline(this.deviceNo, "", "");
        this.deviceStatus = true;


        if(StringUtils.isBlank(deviceNo) || "null".equals(deviceNo)){
            // 发送配置到客户端
            MsgDeviceNo msgDeviceNo = new MsgDeviceNo();
            msgDeviceNo.setDeviceNo(this.deviceNo);

            try {
                sendObject(msgDeviceNo);
            } catch (IOException e) {
                logger.error("", e);
            }
        }

        // 将链接添加到连接池
        pool.addConnection(this);

        // 获取设备的配置数据,并将映射配置发送到客户端
        DeviceMappingService deviceMappingService = SpringUtils.getBean(DeviceMappingService.class);

        List<DeviceMapping> list = deviceMappingService.getDeviceAll(deviceNo);


        for(DeviceMapping entity :list){
            if(!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)){
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgMapping msg = new MsgMapping(data.toJSONString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    msg.write(outputStream);
                    // 包装了Bullet协议的
                    byte[] resultBytes = outputStream.toByteArray();
                    ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                    this.getSession().getBasicRemote().sendBinary(buf);

                } catch (IOException e) {
                    logger.error("", e);
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }
            }

        }

    }


    @OnClose
    public void end() {
        if(this.deviceStatus){ // 正常设备才能移除
            updateOutLine();
            CoonPool pool = SpringUtils.getBean(CoonPool.class);
            pool.removeConnection(this);
        }
    }


    @OnMessage
    public void incoming(byte[] bytes) {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        MsgProxyHttp msg = null;
        try {
            head.read(bis);//读取消息头
            switch (head.getCommand()) {
                case Message.Proxy_Http:// Bind响应命令
                    msg = new MsgProxyHttp(head);
                    msg.read(bis);
                    break;
                case Message.Heart:// 心跳消息
                    MsgHeart msgHeart = new MsgHeart(head);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        msgHeart.write(outputStream);
                        // 包装了Bullet协议的
                        byte[] resultBytes = outputStream.toByteArray();
                        ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                        this.getSession().getBasicRemote().sendPong(buf);
                    } catch (IOException e) {
                        logger.error("", e);
                    } finally {
                        IOUtils.closeQuietly(outputStream);
                    }

                    return;
                case Message.NEW_BINDIP:// 绑定IP
                    MsgBindIP msg2 = new MsgBindIP(head);
                    msg2.read(bis);

                    // 更新设备状态
                    DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);


                    deviceOnlineService.saveOrUpdateOnline(this.deviceNo, msg2.getIp(), msg2.getMac());
                    // 更新IP
                    return;
            }
        } catch (IOException e) {
           logger.error("", e);
        }

    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Bullet Client Error: " + t.toString() );
        if(t != null){
            logger.error("", t);
        }
    }


    /**
     * 更新为离线状态
     */
    private void updateOutLine(){
        logger.warn("数据库操作设备[{}]下线！", this.deviceNo);
        // 更新设备状态
        try {
            DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
            deviceOnlineService.updateOutLine(this.deviceNo);

        } catch (Exception e){  }
    }


    /**
     * 获取会话信息
     * @return
     */
    public Session getSession() {
        return this.session;
    }


    /**
     * 获取设备ID
     * @return
     */
    public String getDeviceNo() {
        return this.deviceNo;
    }

    /**
     * 服务器端主动关闭连接
     */
    public void stop() {
        try {
            this.session.close(
                    new CloseReason(CloseReason.CloseCodes.SERVICE_RESTART,
                            "重启设备！"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 发送数据
     * @param buf
     * @throws IOException
     */
    public void sendBinary(ByteBuffer buf) throws IOException {
        this.session.getBasicRemote().sendBinary(buf,true);
    }


    /**
     * 发送数据到客户端
     * @param message 消息
     * @throws IOException
     */
    private void sendObject(Object message) throws IOException {
        Message message1 = (Message) message;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message1.write(outputStream);
        // 包装了Bullet协议的
        byte[] resultBytes = outputStream.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(resultBytes);
        this.session.getBasicRemote().sendBinary(buf,true);
    }

}
