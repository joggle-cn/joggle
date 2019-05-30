package com.wuweibi.bullet.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.wuweibi.bullet.builder.MapBuilder.newMap;
import static com.wuweibi.bullet.utils.SessionHelper.getUserId;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Slf4j
@Controller
@RequestMapping("/api/user/device/mapping")
public class DeviceMappingController {


    /** 端口映射服务 */
    @Autowired
    private DeviceMappingService deviceMappingService;


    @Autowired
    private CoonPool coonPool;


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delete(HttpServletRequest request,@RequestParam Long id){
        Long userId = getUserId(request);

        boolean status = deviceMappingService.exists(userId, id);

        if(status){

            DeviceMapping entity = deviceMappingService.selectById(id);

            deviceMappingService.deleteById(id);

            String deviceNo = deviceMappingService.getDeviceNo(entity.getDeviceId());
            if(!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)){
                BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
                if(annotation != null){
                    JSONObject data = (JSONObject)JSON.toJSON(entity);
                    MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        msg.write(outputStream);
                        // 包装了Bullet协议的
                        byte[] resultBytes = outputStream.toByteArray();
                        ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                        annotation.getSession().getBasicRemote().sendBinary(buf);

                    } catch (IOException e) {
                        log.error("", e);
                    } finally {
                        IOUtils.closeQuietly(outputStream);
                    }
                }
            }
        }
        return MessageFactory.getOperationSuccess();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Object device(HttpServletRequest request,@RequestParam Long deviceId){
        Long userId = getUserId(request);
        return MessageFactory.get(deviceMappingService.selectByMap(newMap(2)
                .setParam("userId", userId)
                .setParam("device_id", deviceId)
                .build()));
    }


    /**
     * 保存数据
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Object save(
            DeviceMapping entity,
                HttpServletRequest request ){
        Long userId = getUserId(request);
        entity.setUserId(userId);

        boolean status = false;
        if(entity.getId() != null){
            status = deviceMappingService.updateById(entity);
        } else {
            // 验证域名是否被使用
            boolean isOk = deviceMappingService.existsDomain(entity.getDomain());
            if(isOk){
                return MessageFactory.get(State.DomainIsUsed);
            }

            status = deviceMappingService.insert(entity);
        }
        if(status){
            // 发送绑定数据

            String deviceNo = deviceMappingService.getDeviceNo(entity.getDeviceId());
            if(!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)){
                BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
                if(annotation == null){// 设备不在线
                    return MessageFactory.get(State.DeviceNotOnline);
                }

                JSONObject data = (JSONObject)JSON.toJSON(entity);
                MsgMapping msg = new MsgMapping(data.toJSONString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    msg.write(outputStream);
                    // 包装了Bullet协议的
                    byte[] resultBytes = outputStream.toByteArray();
                    ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                    annotation.getSession().getBasicRemote().sendBinary(buf,true);

                } catch (IOException e) {
                    log.error("", e);
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }
            }

            return MessageFactory.getOperationSuccess();
        }
        return MessageFactory.getErrorMessage("服务器错误");
    }



}
