package com.wuweibi.bullet.message.handler;

import com.wuweibi.bullet.protocol.MsgBindIP;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 *
 * joggle 绑定ip
 *
 * @author marker
 * @create 2025-07-08 下午1:13
 **/
@Service
public class MsgBindIPHandler implements MessageHandlerStrategy<MsgBindIP> {

    private static final Logger log = LoggerFactory.getLogger(MsgBindIPHandler.class);

    public MsgBindIPHandler() {
    }

    @Override
    public void handle(MsgBindIP msg) throws IOException {

        // 更新设备状态
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
//                    deviceOnlineService.saveOrUpdateOnline(this.deviceNo, msg2.getIp(), msg2.getMac(), msg2.getVersion());

    }
}
