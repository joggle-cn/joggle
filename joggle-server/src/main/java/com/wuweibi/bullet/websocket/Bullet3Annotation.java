package com.wuweibi.bullet.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.contrast.DeviceOnlineStatus;
import com.wuweibi.bullet.device.contrast.DevicePeerStatusEnum;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.DevicePeersConfigDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingProtocol;
import com.wuweibi.bullet.device.entity.DeviceWhiteIps;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.device.service.DeviceWhiteIpsService;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.message.MessageHandlerContext;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.SpringUtils;
import com.wuweibi.bullet.utils.Utils;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.wuweibi.bullet.protocol.Message.*;


/**
 * 基于Ngrokd的Websocket链接
 * 
 * @author marker
 * @version 1.0
 */
@Slf4j
@ToString
@ServerEndpoint(value = "/inner/open/ws/{tunnelId}", configurator = WebSocketConfigurator.class)
public class Bullet3Annotation {
 

    /**
     * session
     */
    private Session session;

    /**
     * joggled通道ID
     */
    private Integer tunnelId;


    private int index;

    /**
     * 登录的ip地址
     */
    private String ip;





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

//        String version = WebSocketUtil.getHeader( , "version");

        String authorization = (String) session.getUserProperties().get(HttpHeaders.AUTHORIZATION);
        String version = (String) session.getUserProperties().get("version");
        ServerTunnelService serverTunnelService = SpringUtils.getBean(ServerTunnelService.class);

        ServerTunnel serverTunnel = serverTunnelService.getById(tunnelId);
        if (serverTunnel == null) {
            this.stop(CloseReason.CloseCodes.CANNOT_ACCEPT, "server tunnel not found");
        }

        // 校验Token
        if(!serverTunnel.getToken().equals(authorization)){
            log.error("websocket api token error session[{}]", session.getId());
            this.stop(CloseReason.CloseCodes.CANNOT_ACCEPT ,"Auth Token Error...");
            return;
        }

        WebsocketPool pool = SpringUtils.getBean(WebsocketPool.class);
        pool.addConnection(this);

        // 更新服务通道 在线状态
        serverTunnelService.updateStatus(tunnelId, 1, version);
        log.info("websocket[{}] online", tunnelId);
    }


    @OnClose
    public void end(CloseReason closeReason) {
        log.debug("websocket close [{}]", closeReason.toString());
        ServerTunnelService serverTunnelService = SpringUtils.getBean(ServerTunnelService.class);
        WebsocketPool websocketPool = SpringUtils.getBean(WebsocketPool.class);

        if (closeReason.getCloseCode().getCode() == 1001) { // 应用停止时主动关闭
            return;
        }
        websocketPool.removeConnection(this, "normal close");
        serverTunnelService.updateStatus(tunnelId, 0, null);
    }


    /**
     * ngrokd 发送的消息 处理
     * @param bytes
     */
    @OnMessage
    public void incoming(byte[] bytes) {
        MessageHandlerContext messageHandlerContext = SpringUtils.getBean(MessageHandlerContext.class);
        messageHandlerContext.handleMessage(bytes);
    }



    /**
     * 发送映射信息
     */
    @Deprecated
    public void sendMappingInfo(String deviceNo) {
        // 获取设备的配置数据,并将映射配置发送到客户端
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
        DeviceMappingService deviceMappingService = SpringUtils.getBean(DeviceMappingService.class);
        WebsocketPool websocketPool = SpringUtils.getBean(WebsocketPool.class);
        log.info("update device[{}] status=1", deviceNo);
        deviceOnlineService.updateDeviceStatus(deviceNo, DeviceOnlineStatus.ONLINE.status);

        List<DeviceMappingProtocol> list = deviceMappingService.getMapping4ProtocolByDeviceNo(deviceNo);
        for (DeviceMappingProtocol entity : list) {
            if (!StringUtils.isBlank(deviceNo)) {
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgMapping msg = new MsgMapping(data.toJSONString());
                websocketPool.sendMessage(entity.getServerTunnelId(), deviceNo, msg);
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

        // 发送ip白名单信息
        DeviceService deviceService = SpringUtils.getBean(DeviceService.class);
        DeviceDetail deviceDetail = deviceService.getDetailByDeviceNo(deviceNo);
        if (deviceDetail != null) {
            DeviceWhiteIpsService deviceWhiteIpsService = SpringUtils.getBean(DeviceWhiteIpsService.class);
            DeviceWhiteIps deviceWhiteIps = deviceWhiteIpsService.getByDeviceId(deviceDetail.getId());
            if (deviceWhiteIps != null) {
                byte[] data = JSON.toJSONString(deviceWhiteIps.getIps().split(";")).getBytes();
                websocketPool.sendMessageBytes(CONTROL_WHITE_IPS, deviceDetail.getServerTunnelId(), deviceDetail.getDeviceNo(), data);
            }

        }
    }



    @OnError
    public void onError(Throwable t) throws Throwable {
        log.error("Bullet tunnelId[{}] Error: {}", this.tunnelId, t.toString());
////        if (!(t instanceof EOFException)) {
////            log.error("", t);
////        }
//        log.error("", t);
        WebsocketPool pool = SpringUtils.getBean(WebsocketPool.class);
//        if (this.deviceStatus) { // 正常设备才能移除
        pool.removeConnection(this, String.format("异常-%s",t.getMessage()));
//
//        }
//        this.deviceStatus = false;

        // error也会到end去
//        ServerTunnelService serverTunnelService = SpringUtils.getBean(ServerTunnelService.class);
//        serverTunnelService.updateStatus(tunnelId, 0, null);
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
    public void stop(CloseReason.CloseCode closeCode, String message) {
        CloseReason closeReason = new CloseReason(closeCode, message);
        try {
            if (this.session.isOpen()) {
                this.session.close(closeReason);
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }




    /**
     * 发送数据到客户端
     *
     * @param message 消息
     * @throws IOException
     */
    @Deprecated
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
        log.info("sendMessage tunnelId[{}][{}] device[{}] {}", this.tunnelId, this.index, clientNo, msg);
        sendMessage(CONTROL_CLIENT_WRAPPER, clientNo, msg);
    }


    public void sendMessage(int type, String clientNo, Message msg) {
        log.info("Control -> Server -> Client: {} {}", msg.getCommand(),msg.getSequence());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Utils.IntToBytes4(type));
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


    public void sendMessageBytes(int type, String clientNo, byte[] data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Utils.IntToBytes4(type));
            outputStream.write(Utils.IntToBytes4(clientNo.length()));
            outputStream.write(clientNo.getBytes(StandardCharsets.UTF_8));
            outputStream.write(data);
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



    public void sendMessageToServer(Message msg) {
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

    public void stop(String message) {
        this.stop(CloseReason.CloseCodes.NORMAL_CLOSURE, message);
    }

    public Integer getTunnelId() {
        return this.tunnelId;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
