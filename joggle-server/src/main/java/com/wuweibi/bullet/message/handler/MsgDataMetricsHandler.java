package com.wuweibi.bullet.message.handler;

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.metrics.domain.DataMetricsDTO;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import com.wuweibi.bullet.protocol.MsgDataMetrics;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.IOException;

@Service
public class MsgDataMetricsHandler  implements MessageHandlerStrategy<MsgDataMetrics> {

    @Resource
    private DataMetricsService dataMetricsService;
    @Override
    public void handle(MsgDataMetrics msg) throws IOException {
        dataMetricsService.uploadData(JSON.parseObject(msg.getData(), DataMetricsDTO.class));
    }
}
