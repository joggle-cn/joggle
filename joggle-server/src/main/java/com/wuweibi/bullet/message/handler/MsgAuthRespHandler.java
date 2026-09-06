package com.wuweibi.bullet.message.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.contrast.DeviceOnlineStatus;
import com.wuweibi.bullet.device.contrast.DevicePeerStatusEnum;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.DevicePeersConfigDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingProtocol;
import com.wuweibi.bullet.device.entity.DeviceWhiteIps;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.device.service.DeviceWhiteIpsService;
import com.wuweibi.bullet.protocol.MsgAuthResp;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.wuweibi.bullet.protocol.Message.CONTROL_WHITE_IPS;


/**
 * joggle客户端认证结果消息
 *
 * @author marker
 * @version 1.0
 */
@Service
public class MsgAuthRespHandler implements MessageHandlerStrategy<MsgAuthResp> {
    private static final Logger log = LoggerFactory.getLogger(MsgAuthRespHandler.class);

    @Resource
    private DeviceOnlineService deviceOnlineService;

    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource
    private WebsocketPool websocketPool;


    @Override
    public void handle(MsgAuthResp msg) throws IOException {
        String deviceNo = msg.getClientNo();
        // 设备认证成功，需要发送映射信息给这个设备
        // 获取设备的配置数据,并将映射配置发送到客户端
        log.info("update device[{}] status=1", deviceNo);
        deviceOnlineService.updateDeviceStatus(deviceNo, DeviceOnlineStatus.ONLINE.status);

        List<DeviceMappingProtocol> list = deviceMappingService.getMapping4ProtocolByDeviceNo(deviceNo);
        for (DeviceMappingProtocol entity : list) {
            if (!StringUtils.isBlank(deviceNo)) {
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgMapping msgMapping = new MsgMapping(data.toJSONString());
                websocketPool.sendMessage(entity.getServerTunnelId(), deviceNo, msgMapping);
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
}
