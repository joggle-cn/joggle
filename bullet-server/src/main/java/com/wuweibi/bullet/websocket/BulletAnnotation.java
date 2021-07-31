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
import com.wuweibi.bullet.device.domain.dto.DeviceOnlineInfoDTO;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.protocol.*;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.CodeHelper;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static com.wuweibi.bullet.websocket.WebSocketConfigurator.IP_ADDR;


/**
 * Bullet WebSocket服务
 *
 * @author marker
 * @version 1.0
 */
@ServerEndpoint(value = "/tunnel/{deviceNo}", configurator = WebSocketConfigurator.class)
public class BulletAnnotation {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(BulletAnnotation.class);

    /**
     * session
     */
    private Session session;

    // 设备状态
    private boolean deviceStatus = false;
    // 是否认证过
    private boolean authStatus = false;


    /**
     * 设备ID
     */
    private String deviceNo;


    public BulletAnnotation() {
    }


    /**
     * 客户端打开连接
     *
     * @param session session
     */
    @OnOpen
    public void open(Session session,
                     // 设备编号（服务器端生成)
                     @PathParam("deviceNo") String deviceNo) {
        this.session = session;
        this.deviceNo = deviceNo;


        // 如果是首次链接，执行重新分配设备编码
        if (StringUtils.isBlank(deviceNo) || "null".equals(deviceNo)) {
            this.deviceNo = CodeHelper.makeNewCode();

            // 发送配置到客户端
            MsgDeviceNo msgDeviceNo = new MsgDeviceNo(this.deviceNo);
            try {
                sendObject(msgDeviceNo);
            } catch (IOException e) {
                logger.error("", e);
            }

            // 待绑定的设备，无需认证执行上线
            deviceOnline();
            return;
        }

        // 检查是否绑定
        DeviceService deviceService = SpringUtils.getBean(DeviceService.class);
        Device device = deviceService.getByDeviceNo(this.deviceNo);
        if (device == null) { // 未绑定
            // 待绑定的设备，无需认证执行上线
            deviceOnline();
        }

        // 需要认证(客户端主动发起)



    }


    @OnClose
    public void end(CloseReason closeReason) {
        // 这里是由于设备已经在线，其他设备将不允许关闭唯一的在线设备接入信息。
//        if (CloseReason.CloseCodes.NOT_CONSISTENT.equals(closeReason.getCloseCode())) {
//            logger.warn("CloseReason({}) deviceNo=[{}] is online, sessionId[{}] is closed!",
//                    CloseReason.CloseCodes.NOT_CONSISTENT.getCode(), this.deviceNo, this.session.getId());
//            return;
//        }

        logger.warn("BulletAnnotation deviceNo={} close");
        logger.warn("BulletAnnotation deviceNo={},status={}", this.deviceNo, this.deviceStatus);
        if (this.deviceStatus) { // 正常设备才能移除
            updateOutLine();
            this.deviceStatus = false;
            CoonPool pool = SpringUtils.getBean(CoonPool.class);
            pool.removeConnection(this);
        }
    }


    @OnMessage
    public void incoming(byte[] bytes) {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        try {
            head.read(bis);//读取消息头
            switch (head.getCommand()) {
                case Message.Proxy_Http:// Bind响应命令

                    MsgProxyHttp msgProxyHttp = new MsgProxyHttp(head);
                    msgProxyHttp.read(bis);
                    break;
                case Message.AUTH:// 认证
                    MsgAuth msgAuth = new MsgAuth(head);
                    msgAuth.read(bis);

                    deviceAuth(msgAuth);
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
                case Message.LOG_MAPPING_LOG:// 日志消息
                    MsgCommandLog msgCommandLog = new MsgCommandLog(head);
                    msgCommandLog.read(bis);
                    // 转移消息到另外一个通道

                    LogAnnotation.broadcast(msgCommandLog.getMappingId(), msgCommandLog.getLine());
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            IOUtils.closeQuietly(bis);
        }

    }

    private void deviceAuth(MsgAuth msgAuth) {
        String authToken = msgAuth.getAuthToken();
        logger.debug("device auth deviceNo={} authToken={}", this.deviceNo, authToken);

        DeviceService deviceService = SpringUtils.getBean(DeviceService.class);
        Device device = deviceService.getByDeviceNo(this.deviceNo);
        String msg = "Device AuthToken Error!!!";

        // 认证成功 发送映射信息
        if (device != null && device.getDeviceSecret() != null && device.getDeviceSecret().equals(authToken)) {

            // 设备上线（包含设备校验）
            deviceOnline();
            if (!this.deviceStatus) { // 如果没有上线成功
                MsgAuthResp authResp = new MsgAuthResp("Device " + this.deviceNo + " is Online!!!");
                sendMessage(authResp);
                return;
            }
            sendMappingInfo();
            msg = "SUCCESS";
        }


        // 发送认证失败消息
        MsgAuthResp authResp = new MsgAuthResp(msg);
        sendMessage(authResp);

    }


    /**
     * 发送映射信息
     */
    private void sendMappingInfo() {
        // 获取设备的配置数据,并将映射配置发送到客户端
        DeviceMappingService deviceMappingService = SpringUtils.getBean(DeviceMappingService.class);
        List<DeviceMapping> list = deviceMappingService.getDeviceAll(deviceNo);

        for (DeviceMapping entity : list) {
            if (!StringUtils.isBlank(deviceNo)) {
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
                }
            }
        }


    }

    /**
     * 设备上线
     */
    private void deviceOnline() {
        // 因为认证通过了，采用剔除旧链接方式 可能会导致两个设备争夺链接的情况
        // 因此采用后认证者直接掉线方式
        CoonPool pool = SpringUtils.getBean(CoonPool.class);

        // 更新设备状态
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
        if (pool.exists(deviceNo)) {
            logger.warn("{} 设备已经在线", deviceNo);
            // 这里判断的前提是设备被绑定后，不能有其他设备用同样的NO链接
            try {
                this.session.close(new CloseReason(CloseReason.
                        CloseCodes.NOT_CONSISTENT,
                        deviceNo + " deviceNo is online! please try another deviceNo."));
            } catch (IOException e) {
                logger.error("", e);
            }
            return;
        }

        String publicIp = getRemoteAddress(this.session);
        // 设备在线
        DeviceOnlineInfoDTO deviceOnlineInfoDTO = new DeviceOnlineInfoDTO();
        deviceOnlineInfoDTO.setDeviceNo(deviceNo);
        deviceOnlineInfoDTO.setPublicIp(publicIp);
        deviceOnlineService.saveOrUpdate(deviceOnlineInfoDTO);
        this.deviceStatus = true;

        // 将链接添加到连接池
        pool.addConnection(this);
    }


    @SneakyThrows
    public static String getRemoteAddress(final Session session) {
        return (String) session.getUserProperties().get(IP_ADDR);
    }



    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Bullet Client Error: " + t.toString());
        if (!(t instanceof EOFException)) {
            logger.error("", t);
        }
        CoonPool pool = SpringUtils.getBean(CoonPool.class);
        pool.removeConnection(this);
        if (this.deviceStatus) { // 正常设备才能移除
            updateOutLine();
        }
        this.deviceStatus = false;
    }


    /**
     * 更新为离线状态
     */
    private void updateOutLine() {
        logger.warn("数据库操作设备[{}]下线！", this.deviceNo);
        // 更新设备状态
        try {
            DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
            deviceOnlineService.updateOutLine(this.deviceNo);

        } catch (Exception e) {
        }
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
     * 获取设备ID
     *
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
     *
     * @param buf
     * @throws IOException
     */
    public void sendBinary(ByteBuffer buf) throws IOException {
        this.session.getBasicRemote().sendBinary(buf, true);
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
    public void sendMessage(Message msg) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        msg.write(outputStream);
        // 包装了Bullet协议的
        byte[] resultBytes = outputStream.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(resultBytes);
        this.session.getBasicRemote().sendBinary(buf, true);
    }
}
