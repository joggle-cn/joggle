package com.wuweibi.bullet.message.handler;

import com.wuweibi.bullet.protocol.MsgDeviceScanResp;
import com.wuweibi.bullet.protocol.domain.KscanResult;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import com.wuweibi.bullet.service.DeviceMappingService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.IOException;

/**
 * 设备扫描结果
 * @author marker on 2025/7/8.
 */
@Service
public class MsgDeviceScanRespHandler implements MessageHandlerStrategy<MsgDeviceScanResp> {

    @Resource
    private DeviceMappingService deviceMappingService;

    @Override
    public void handle(MsgDeviceScanResp msg) throws IOException {
        String  scanDeviceNo = msg.getDeviceNo();
        KscanResult scanJson = msg.getResult();

        // 这里存在问题 就是不知设备id是多少，就不知需要更新那个设备下的映射信息。
        deviceMappingService.putScanResult(scanDeviceNo, scanJson);
    }
}
