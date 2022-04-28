package com.wuweibi.bullet.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDelDTO;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.mapper.DomainMapper;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

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
    public Object delete(@JwtUser Session session, @RequestBody DeviceMappingDelDTO dto){
        Long userId = session.getUserId();
        Long dmId = dto.getId();
        // 验证设备映射是自己的
        boolean status = deviceMappingService.exists(userId, dmId);
        if(status){
            DeviceMapping entity = deviceMappingService.getById(dmId);
            deviceMappingService.removeById(dmId);

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
    public R<List<DeviceMapping>> device(@JwtUser Session session, @RequestParam Long deviceId){
        Long userId = session.getUserId();
        return R.success(deviceMappingService.listByMap(newMap(2)
                .setParam("userId", userId)
                .setParam("device_id", deviceId)
                .build()));
    }

    @Resource
    private DomainMapper domainMapper;
    @Resource
    private UserFlowService userFlowService;

    /**
     * 保存或者更新数据
     * @param entity
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public R save(@JwtUser Session session, DeviceMapping entity ){
        Long userId = session.getUserId();
        entity.setUserId(userId);

        DeviceMapping deviceMapping = deviceMappingService.getById(entity.getId());
        entity.setDomainId(deviceMapping.getDomainId());
        entity.setDomain(deviceMapping.getDomain());
        entity.setPort(entity.getPort());
        entity.setProtocol(entity.getProtocol());
        entity.setRemotePort(deviceMapping.getRemotePort());

        // 验证设备映射是自己的
        if(!deviceMappingService.exists(userId, entity.getId())){
            return R.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
        }
        // 判断映射的域名是否过期，过期后不允许开启
        if(!domainMapper.checkDoaminIdDue(deviceMapping.getDomainId())){
            if(entity.getStatus() == 1){ // 不能启用
                return R.fail(SystemErrorType.DOMAIN_IS_DUE);
            }
        }

        // 如果没有流量了，不能操作映射，会有一个缓冲过程
        if(!userFlowService.hasFlow(userId)){
            return R.fail(SystemErrorType.FLOW_IS_DUE);
        }

        if(entity.getId() != null){
            deviceMappingService.updateById(entity);
        } else {
            // 验证域名是否被使用
            boolean isOk = deviceMappingService.existsDomain(entity.getDomain());
            if(isOk){
                return R.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
            }
            deviceMappingService.save(entity);
        }


        String deviceNo = deviceMappingService.getDeviceNo(entity.getDeviceId());
        if(!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)){
            BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
            if(annotation == null){// 设备不在线
                return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
            }

            JSONObject data = (JSONObject)JSON.toJSON(entity);

            Message msg;

            if(entity.getStatus() == 1) { // 启用映射
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
            return R.success();
        }
        return R.fail("服务器错误");
    }



}
