package com.wuweibi.bullet.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.protocol.MsgDeviceLatency;
import com.wuweibi.bullet.protocol.strategy.MessageHandlerStrategy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class MsgDeviceLatencyHandler implements MessageHandlerStrategy<MsgDeviceLatency> {

    private static final String LATENCY_REDIS_KEY_PREFIX = "device:latency:";

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void handle(MsgDeviceLatency msg) throws IOException {
        String data = msg.getData();
        if (data == null) {
            return;
        }
        JSONObject json = JSONObject.parseObject(data);
        String deviceNo = json.getString("deviceNo");
        Long latencyMs = json.getLong("latencyMs");
        if (deviceNo == null || latencyMs == null) {
            return;
        }
        stringRedisTemplate.opsForValue().set(
                LATENCY_REDIS_KEY_PREFIX + deviceNo,
                String.valueOf(latencyMs),
                5,
                TimeUnit.MINUTES
        );
    }
}
