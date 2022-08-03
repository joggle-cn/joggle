package com.wuweibi.bullet.device.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.device.domain.dto.DeviceDoorDTO;
import com.wuweibi.bullet.device.entity.DeviceDoor;
import com.wuweibi.bullet.device.service.DeviceDoorService;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgDeviceDoor;
import com.wuweibi.bullet.protocol.domain.DoorConfig;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

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
    private CoonPool coonPool;

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
        Device device = deviceService.getById(dto.getDeviceId());
        if (device == null) {
            return R.fail("设备不存在");
        }
        if (!device.getUserId().equals(userId)) {
            return R.fail("设备不存在");
        }

        // TODO 验证domainId是否绑定

        // TODO 调用绑定映射关系


        // 查询设备是否存在任意门配置
        DeviceDoor deviceDoor = this.deviceDoorService.getByDeviceId(dto.getDeviceId());
        if (deviceDoor == null) {
            deviceDoor = new DeviceDoor();
            deviceDoor.setCreateTime(new Date());
            deviceDoor.setDeviceId(dto.getDeviceId());
            deviceDoor.setServerPath("/");
        }
        deviceDoor.setEnable(dto.getEnable());
        deviceDoor.setLocalPath(dto.getLocalPath());
        deviceDoor.setUpdateTime(new Date());
        boolean status = this.deviceDoorService.saveOrUpdate(deviceDoor);

        // 设备发送消息开启任意门
        String deviceNo = device.getDeviceNo();
        BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
        if (annotation != null) {
            DoorConfig doorConfig = new DoorConfig();
            doorConfig.setDeviceId(device.getId());
            doorConfig.setLocalPath(deviceDoor.getLocalPath());
            doorConfig.setServerPath(deviceDoor.getServerPath());
            doorConfig.setEnable(deviceDoor.getEnable());

            JSONObject data = (JSONObject) JSON.toJSON(doorConfig);
            MsgDeviceDoor msg = new MsgDeviceDoor(data.toJSONString());
            annotation.sendMessage(msg);
        }
        return R.ok(status);
    }


}
