package com.wuweibi.bullet.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.business.DeviceBiz;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingUpdateDTO;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.domain.DeviceMappingDTO;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class DeviceBizImpl implements DeviceBiz {


    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private WebsocketPool websocketPool;

    @Resource
    private DevicePeersService devicePeersService;

    @Override
    @Async
    public void closeAllMappingByUserId(Long userId) {
        if (deviceMappingService.hasOkMapping(userId)) {
            List<DeviceMappingDTO> list = deviceMappingService.getAllByUserId(userId);

            // 批量关闭映射
            deviceMappingService.updateDownByUserId(userId);

            for (DeviceMappingDTO entity : list) {
                entity.setStatus(0);
                Integer serverTunnelId = entity.getServerTunnelId();
                String deviceNo = entity.getDeviceNo();
                if (!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)) {
                    JSONObject data = (JSONObject) JSON.toJSON(entity);
                    Message msg;
                    log.debug("设备 {} 停用 {} 映射", entity.getDeviceId(), entity.getId());
                    msg = new MsgUnMapping(data.toJSONString());
                    // 发送信息给设备
                    websocketPool.sendMessage(serverTunnelId, deviceNo, msg);
                }
            }
        } else {
            log.info("userId={} not device mapping", userId);
        }

        devicePeersService.closeRelayPeersByUserId(userId);
    }

    @Override
    public R updateMapping(DeviceMappingUpdateDTO deviceMappingDTO) {

        // TODO 更新映射信息



        return null;
    }
}
