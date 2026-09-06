package com.wuweibi.bullet.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.device.contrast.DeviceOnlineStatus;
import com.wuweibi.bullet.protocol.MsgGetDeviceStatusResp;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import com.wuweibi.bullet.service.DeviceOnlineService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class MsgGetDeviceStatusRespHandler implements MessageHandlerStrategy<MsgGetDeviceStatusResp> {

    @Resource
    private DeviceOnlineService deviceOnlineService;



    @Override
    public void handle(MsgGetDeviceStatusResp msg) throws IOException {
        JSONObject jsonObject = msg.getData();
        // 更新在线状态
        List<String> deviceNoList = new ArrayList<>(jsonObject.size());
        jsonObject.forEach((item, v)->{
            deviceNoList.add(item);
        });
        // TODO this.tunnelId待确认
//        deviceOnlineService.updateOutLineByTunnelId(this.tunnelId);
        deviceOnlineService.batchUpdateStatus(deviceNoList, DeviceOnlineStatus.ONLINE.status);

    }
}
