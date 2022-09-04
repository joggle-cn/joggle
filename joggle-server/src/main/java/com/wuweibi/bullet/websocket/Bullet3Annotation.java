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
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.contrast.DeviceOnlineStatus;
import com.wuweibi.bullet.device.contrast.DevicePeerStatusEnum;
import com.wuweibi.bullet.device.domain.DevicePeersConfigDTO;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.protocol.*;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.wuweibi.bullet.protocol.Message.CONTROL_CLIENT_WRAPPER;
import static com.wuweibi.bullet.protocol.Message.CONTROL_SERVER_WRAPPER;
import static com.wuweibi.bullet.websocket.WebSocketConfigurator.IP_ADDR;


/**
 * Bullet WebSocket服务
 *
 * @author marker
 * @version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/inner/open/ws/{tunnelId}", configurator = WebSocketConfigurator.class)
public class Bullet3Annotation {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(Bullet3Annotation.class);

    /**
     * session
     */
    private Session session;

    /**
     * 设备ID
     */
    private Integer tunnelId;



    public Bullet3Annotation() {
    }



    /**
     * 客户端打开连接
     *
     * @param session session
     * @param tunnelId 通道id
     */
    @OnOpen
    public void open(Session session, @PathParam("tunnelId") Integer tunnelId) {
        session.setMaxIdleTimeout(10000l); // 超时时间10s
        this.session = session;
        this.tunnelId = tunnelId;

        WebsocketPool pool = SpringUtils.getBean(WebsocketPool.class);
        pool.addConnection(this);

//        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
//        deviceOnlineService.checkDeviceStatus();
    }


    @OnClose
    public void end(CloseReason closeReason) {
        // 这里是由于设备已经在线，其他设备将不允许关闭唯一的在线设备接入信息。
//        if (CloseReason.CloseCodes.NOT_CONSISTENT.equals(closeReason.getCloseCode())) {
//            logger.warn("CloseReason({}) deviceNo=[{}] is online, sessionId[{}] is closed!",
//                    CloseReason.CloseCodes.NOT_CONSISTENT.getCode(), this.deviceNo, this.session.getId());
//            return;
//        }
//        logger.warn("BulletAnnotation close({}) deviceNo={},status={}",
//                closeReason.toString(), this.deviceNo, this.deviceStatus);
//        if (this.deviceStatus) { // 正常设备才能移除
//            updateOutLine();
//            this.deviceStatus = false;
//            CoonPool pool = SpringUtils.getBean(CoonPool.class);
//            pool.removeConnection(this, String.format("异常-%s", closeReason.toString()));
//        }
    }


    @OnMessage
    public void incoming(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        try {
            head.read(bis);//读取消息头
            switch (head.getCommand()) {
                case Message.PROXY:// Bind响应命令
                    MsgProxy msgProxy = new MsgProxy(head);
                    msgProxy.read(bis);
                    break;
                case Message.AUTH_RESP:// 认证成功
                    MsgAuthResp msgAuthResp = new MsgAuthResp(head);
                    msgAuthResp.read(bis);

                    String clientNo = msgAuthResp.getClientNo();

                    this.sendMappingInfo(clientNo);

                    break;
                case Message.AUTH:// 认证
                    MsgAuth msgAuth = new MsgAuth(head);
                    msgAuth.read(bis);
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
//                    deviceOnlineService.saveOrUpdateOnline(this.deviceNo, msg2.getIp(), msg2.getMac(), msg2.getVersion());

                    return;
                case Message.GET_DEVICE_STATUS_RESP:// 获取设备状态响应所有设备状态
                    MsgGetDeviceStatusResp msgGetDeviceStatusResp = new MsgGetDeviceStatusResp(head);
                    msgGetDeviceStatusResp.read(bis);
                    JSONObject jsonObject = msgGetDeviceStatusResp.getData();
                    // 更新在线状态
                    deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
                    List<String> deviceNoList = new ArrayList<>(jsonObject.size());
                    jsonObject.forEach((item, v)->{
                        deviceNoList.add(item);
                    });
                    deviceOnlineService.batchUpdateStatus(deviceNoList, DeviceOnlineStatus.ONLINE.status);

                    return;
                case Message.DEVICE_DOWN: // 设备下线
                    MsgDeviceDown msgDeviceDown = new MsgDeviceDown(head);
                    msgDeviceDown.read(bis);
                    String deviceNo = msgDeviceDown.getDeviceNo();
                    deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
                    deviceOnlineService.updateDeviceStatus(deviceNo, DeviceOnlineStatus.OUTLINE.status);
                    return;
//                case Message.LOG_MAPPING_LOG:// 日志消息
//                    MsgCommandLog msgCommandLog = new MsgCommandLog(head);
//                    msgCommandLog.read(bis);
//                    // 转移消息到另外一个通道
//
//                    LogAnnotation.broadcast(this.deviceId, msgCommandLog.getLine());
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            IOUtils.closeQuietly(bis);
        }

    }



    /**
     * 发送映射信息
     */
    public void sendMappingInfo(String deviceNo) {
        // 获取设备的配置数据,并将映射配置发送到客户端
        DeviceMappingService deviceMappingService = SpringUtils.getBean(DeviceMappingService.class);
        List<DeviceMapping> list = deviceMappingService.getDeviceAll(deviceNo);

        for (DeviceMapping entity : list) {
            if (!StringUtils.isBlank(deviceNo)) {
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgMapping msg = new MsgMapping(data.toJSONString());
                this.sendMessage(deviceNo, msg);
            }
        }

        // P2P 设备通道
        DevicePeersService devicePeersService = SpringUtils.getBean(DevicePeersService.class);
        List<DevicePeersConfigDTO> peersList = devicePeersService.getListByDeviceNo(deviceNo);
        for (DevicePeersConfigDTO configDTO : peersList){
            if (configDTO.getStatus() == DevicePeerStatusEnum.DISABLE.getStatus()) {
                continue;
            }
            devicePeersService.sendMsgPeerConfig(configDTO);
        }

    }



    @SneakyThrows
    public static String getRemoteAddress(final Session session) {
        return (String) session.getUserProperties().get(IP_ADDR);
    }


    @OnError
    public void onError(Throwable t) throws Throwable {
//        logger.error("Bullet Client[{}] Error: {}", this.deviceNo, t.toString());
////        if (!(t instanceof EOFException)) {
////            logger.error("", t);
////        }
//        logger.error("", t);
        WebsocketPool pool = SpringUtils.getBean(WebsocketPool.class);
//        if (this.deviceStatus) { // 正常设备才能移除
            pool.removeConnection(this, String.format("异常-%s",t.getMessage()));
//
//        }
//        this.deviceStatus = false;
    }




    /**
     * 获取会话信息
     *
     * @return
     */
    public Session getSession() {
        return this.session;
    }



    /**
     * 服务器端主动关闭连接
     */
    public void stop(String message) {
        try {
            if (this.session.isOpen()) {
                this.session.close(
                        new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,
                                "服务器主动关闭，原因: "+ message));
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }




    /**
     * 发送数据到客户端
     *
     * @param message 消息
     * @throws IOException
     */
    public void sendObject(Object message) throws IOException {
        Message message1 = (Message) message;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message1.write(outputStream);
        // 包装了Bullet协议的
        byte[] resultBytes = outputStream.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(resultBytes);
        this.session.getBasicRemote().sendBinary(buf, true);
    }


    /**
     * 发送消息
     *
     * @param msg
     */
    @SneakyThrows
    public void sendMessage(String clientNo, Message msg) {
        log.info("Control -> Server -> Client: {} {}", msg.getCommand(),msg.getSequence());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Utils.IntToBytes4(CONTROL_CLIENT_WRAPPER));
            outputStream.write(Utils.IntToBytes4(clientNo.length()));
            outputStream.write(clientNo.getBytes(StandardCharsets.UTF_8));
            msg.write(outputStream);
            // 包装了Bullet协议的
            byte[] resultBytes = outputStream.toByteArray();
            ByteBuffer buf = ByteBuffer.wrap(resultBytes);
            this.session.getBasicRemote().sendBinary(buf, true);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }



    public void sendMessageToServer(MsgGetDeviceStatus msg) {
        log.info("Control -> Server: {} {}", msg.getCommand(),msg.getSequence());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Utils.IntToBytes4(CONTROL_SERVER_WRAPPER));
            msg.write(outputStream);
            // 包装了Bullet协议的
            byte[] resultBytes = outputStream.toByteArray();
            ByteBuffer buf = ByteBuffer.wrap(resultBytes);
            this.session.getBasicRemote().sendBinary(buf, true);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

    }
}
