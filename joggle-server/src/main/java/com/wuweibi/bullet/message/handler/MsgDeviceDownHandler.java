package com.wuweibi.bullet.message.handler;

import com.wuweibi.bullet.device.contrast.DeviceOnlineStatus;
import com.wuweibi.bullet.protocol.MsgDeviceDown;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import com.wuweibi.bullet.service.DeviceOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;


/**
 *
 * joggle客户端认证消息
 *
 * @author marker
 * @create 2025-07-08 下午1:13
 **/
@Service
public class MsgDeviceDownHandler implements MessageHandlerStrategy<MsgDeviceDown> {

    private static final Logger log = LoggerFactory.getLogger(MsgDeviceDownHandler.class);

     @Resource
     private DeviceOnlineService deviceOnlineService;

    @Override
    public void handle(MsgDeviceDown msg) throws IOException {
        String deviceNo = msg.getDeviceNo();
        deviceOnlineService.updateDeviceStatus(deviceNo, DeviceOnlineStatus.OUTLINE.status);

    }
}
