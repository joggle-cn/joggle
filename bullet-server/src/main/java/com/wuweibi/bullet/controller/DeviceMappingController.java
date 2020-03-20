package com.wuweibi.bullet.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.mapper.DomainMapper;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.wuweibi.bullet.core.builder.MapBuilder.newMap;

/**
 * <p>
 *  设备映射信息 前端控制器
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Slf4j
@RestController
@RequestMapping("/api/user/device/mapping")
public class DeviceMappingController {


    /** 端口映射服务 */
    @Resource
    private DeviceMappingService deviceMappingService;


    @Resource
    private CoonPool coonPool;


    /**
     * 删除映射关系
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Object delete(@JwtUser Session session, @RequestParam Long id){
        Long userId = session.getUserId();

        // 验证设备映射是自己的
        boolean status = deviceMappingService.exists(userId, id);
        if(status){

            DeviceMapping entity = deviceMappingService.getById(id);
            deviceMappingService.removeById(id);

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


    /**
     * 获取映射详情
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object device(@JwtUser Session session,@RequestParam Long deviceId){
        Long userId = session.getUserId();
        return MessageFactory.get(deviceMappingService.listByMap(newMap(2)
                .setParam("userId", userId)
                .setParam("device_id", deviceId)
                .build()));
    }

    @Autowired
    private DomainMapper domainMapper;

    /**
     * 保存数据
     * @param entity
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Result save(@JwtUser Session session, DeviceMapping entity ){
        Long userId = session.getUserId();
        entity.setUserId(userId);

        DeviceMapping deviceMapping = deviceMappingService.getById(entity.getId());
        entity.setDomainId(deviceMapping.getDomainId());

        // 验证设备映射是自己的
        if(!deviceMappingService.exists(userId, entity.getId())){
            return Result.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
        }
        // 判断映射的域名是否过期，过期后不允许开启
        if(!domainMapper.checkDoaminIdDue(deviceMapping.getDomainId())){
            if(entity.getStatus() == 1){ // 不能启用
                return Result.fail(SystemErrorType.DOMAIN_IS_DUE);
            }
        }

        boolean status = false;
        if(entity.getId() != null){
            status = deviceMappingService.updateById(entity);
        } else {
            // 验证域名是否被使用
            boolean isOk = deviceMappingService.existsDomain(entity.getDomain());
            if(isOk){
                return Result.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
            }

            status = deviceMappingService.save(entity);
        }
        if(status){
            // 发送绑定数据
            int mappingStatus = entity.getStatus();


            String deviceNo = deviceMappingService.getDeviceNo(entity.getDeviceId());
            if(!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)){
                BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
                if(annotation == null){// 设备不在线
                    return Result.fail(SystemErrorType.DEVICE_NOT_ONLINE);
                }

                JSONObject data = (JSONObject)JSON.toJSON(entity);

                Message msg;

                if(mappingStatus == 1) { // 启用映射
                    msg = new MsgMapping(data.toJSONString());
                } else {
                    log.debug("设备 {} 停用 {} 映射", entity.getDeviceId(), entity.getId());
                    msg = new MsgUnMapping(data.toJSONString());
                }

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

            return Result.success();
        }
        return Result.fail("服务器错误");
    }



}
