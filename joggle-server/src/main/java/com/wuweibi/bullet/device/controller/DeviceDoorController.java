package com.wuweibi.bullet.device.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceDoorDTO;
import com.wuweibi.bullet.device.domain.vo.DeviceDoorVO;
import com.wuweibi.bullet.device.entity.DeviceDoor;
import com.wuweibi.bullet.device.service.DeviceDoorService;
import com.wuweibi.bullet.domain2.controller.DomainController;
import com.wuweibi.bullet.domain2.domain.DomainDetail;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgDeviceDoor;
import com.wuweibi.bullet.protocol.domain.DoorConfig;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 设备任意门(DeviceDoor)表控制层
 *
 * @author marker
 * @since 2022-08-03 21:21:27
 */
@Slf4j
@RestController
@Api(value = "设备任意门", tags = "设备任意门")
@RequestMapping("/api/device/door")
public class DeviceDoorController {
    /**
     * 服务对象
     */
    @Resource
    private DeviceDoorService deviceDoorService;

    @Resource
    private DeviceService deviceService;
    @Resource
    private DomainService domainService;

    @Resource
    private WebsocketPool coonPool;

    @Resource
    private DeviceMappingService deviceMappingService;

    /**
     * 配置任意门
     *
     * @param dto 实体对象
     * @return 新增结果
     */
    @ApiOperation("配置任意门")
    @PostMapping("/config/one")
    @Transactional
    public R<Boolean> save(@RequestBody @Valid DeviceDoorDTO dto) {
        Long userId = SecurityUtils.getUserId();
        Long domainId = dto.getDomainId();
        DeviceDetail deviceDetail = deviceService.getDetail(dto.getDeviceId());
        if (deviceDetail == null) {
            return R.fail("设备不存在");
        }
        String deviceNo = deviceDetail.getDeviceNo();
        if (!deviceDetail.getUserId().equals(userId)) {
            return R.fail("设备不存在");
        }

        // 校验domainid是否自己的
        if(!domainService.exists(userId, domainId)){
            return R.fail("域名不存在");
        }

        // 查询设备是否存在任意门配置
        DeviceDoor deviceDoor = this.deviceDoorService.getByDeviceId(dto.getDeviceId());
        if (deviceDoor == null) {
            deviceDoor = new DeviceDoor();
            deviceDoor.setCreateTime(new Date());
            deviceDoor.setDeviceId(dto.getDeviceId());
            deviceDoor.setServerPath("/");
        }
        deviceDoor.setEnable(dto.getEnable());
        deviceDoor.setDomainId(dto.getDomainId());
        deviceDoor.setLocalPath(dto.getLocalPath());
        deviceDoor.setUpdateTime(new Date());

        // 验证domainId是否绑定
        if (!Objects.equals(domainId, deviceDoor.getDomainId())  && deviceMappingService.existsDomainId(domainId)) {
            return R.fail("域名已绑定");
        }

        boolean status = this.deviceDoorService.saveOrUpdate(deviceDoor);

        // 设备发送消息开启任意门
        Bullet3Annotation annotation = coonPool.getByTunnelId(deviceDetail.getServerTunnelId());
        if (annotation != null) {
            DoorConfig doorConfig = new DoorConfig();
            doorConfig.setDeviceId(deviceDetail.getId());
            doorConfig.setLocalPath(deviceDoor.getLocalPath());
            doorConfig.setServerPath(deviceDoor.getServerPath());
            doorConfig.setEnable(deviceDoor.getEnable());

            JSONObject data = (JSONObject) JSON.toJSON(doorConfig);
            MsgDeviceDoor msg = new MsgDeviceDoor(data.toJSONString());
            annotation.sendMessage(deviceNo, msg);
        }

        // 调用绑定映射关系
        DomainDetail domainDetail = domainService.getDetail(domainId);

        DeviceMapping deviceMapping = new DeviceMapping();
        deviceMapping.setDeviceId(dto.getDeviceId());
        deviceMapping.setDomainId(dto.getDomainId());
        deviceMapping.setUserId(userId);
        deviceMapping.setCreateTime(new Date());
        deviceMapping.setProtocol(4);
        deviceMapping.setDomain(domainDetail.getDomainFull());
        deviceMapping.setPort(4040);
        deviceMapping.setHost("127.0.0.1");
        deviceMapping.setDescription("任意门");
        deviceMapping.setStatus(dto.getEnable());

        R<Long> r1 = domainController.bind(dto.getDomainId(), dto.getDeviceId());
        deviceMapping.setId(r1.getData());
        R r2 = deviceMappingController.save(deviceMapping);
        if (r2.isFail()) {
            return r2;
        }
        return R.ok(status);
    }

    @Resource
    private DeviceMappingController deviceMappingController;
    @Resource
    private DomainController domainController;


    /**
     * 获取配置任意门
     * @return 新增结果
     */
    @ApiOperation("获取配置任意门")
    @GetMapping("/config/one")
    public R<DeviceDoorVO> detail(
            @ApiParam("设备id")
            @RequestParam Long deviceId) {
        Long userId = SecurityUtils.getUserId();
        Device device = deviceService.getById(deviceId);
        if (device == null) {
            return R.fail("设备不存在");
        }
        if (!device.getUserId().equals(userId)) {
            return R.fail("设备不存在");
        }


        DeviceDoor deviceDoor = this.deviceDoorService.getByDeviceId(deviceId);
        if (deviceDoor == null){
            return R.fail("无配置信息");
        }

        DomainDetail domain = this.domainService.getDetail(deviceDoor.getDomainId());


        DeviceDoorVO dto = new DeviceDoorVO();
        dto.setDeviceId(deviceId);
        dto.setDomainId(deviceDoor.getDomainId());
        dto.setLocalPath(deviceDoor.getLocalPath());
        dto.setEnable(deviceDoor.getEnable());
        if (domain != null) {
            dto.setDomain(domain.getDomainFull());
        }

        return R.ok(dto);
    }


}
