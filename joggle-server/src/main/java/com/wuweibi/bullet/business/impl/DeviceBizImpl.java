package com.wuweibi.bullet.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.business.DeviceBiz;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.DeviceMappingDTO;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Slf4j
@Service
public class DeviceBizImpl implements DeviceBiz {


    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private CoonPool coonPool;

    @Override
    public void closeAllMappingByUserId(Long userId) {

        // 如果用户有在线设备
        if(!deviceMappingService.hasOkMapping(userId)){
            log.debug("userId={} not device mapping", userId);
            return;
        }

        List<DeviceMappingDTO> list = deviceMappingService.getAllByUserId(userId);

        // 批量关闭映射
        deviceMappingService.updateDownByUserId(userId);

        for(DeviceMappingDTO entity : list){
            entity.setStatus(0);
            String deviceNo = entity.getDeviceNo();
            if(!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)){
                BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
                if(annotation == null){// 设备不在线
                    continue;
                }

                JSONObject data = (JSONObject) JSON.toJSON(entity);

                Message msg;
                log.debug("设备 {} 停用 {} 映射", entity.getDeviceId(), entity.getId());
                msg = new MsgUnMapping(data.toJSONString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    msg.write(outputStream);
                    // 包装了Bullet协议的
                    byte[] resultBytes = outputStream.toByteArray();
                    ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                    annotation.sendBinary(buf);
                } catch (IOException e) {
                    log.error("", e);
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }

            }
        }



    }
}
