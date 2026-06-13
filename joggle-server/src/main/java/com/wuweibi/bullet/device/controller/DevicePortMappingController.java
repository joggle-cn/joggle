package com.wuweibi.bullet.device.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingPortDTO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingClientVO;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.service.DeviceMappingManagerService;
import com.wuweibi.bullet.device.service.DeviceMappingViewService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "设备端口映射")
@WebApi
@RestController
@RequestMapping("/api/user/device/mapping/port")
public class DevicePortMappingController {

    @Resource
    private DeviceMappingManagerService deviceMappingManagerService;
    @Resource
    private DeviceMappingViewService deviceMappingViewService;
    @Resource
    private DeviceService deviceService;

    @ApiOperation("保存或更新端口映射")
    @PostMapping("")
    public R save(@JwtUser Session session, @RequestBody @Valid DeviceMappingPortDTO dto) {
        Long userId = session.getUserId();
        synchronized (userId) {
            return deviceMappingManagerService.savePortMapping(userId, dto);
        }
    }

    @ApiOperation("端口映射列表")
    @GetMapping("/list")
    public R<List<DeviceMappingClientVO>> list(@JwtUser Session session, @RequestParam Long deviceId) {
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        if (deviceInfo == null) {
            return R.fail("设备不存在");
        }
        if (!session.getUserId().equals(deviceInfo.getUserId())) {
            return R.fail("用户设备不存在");
        }
        return R.success(deviceMappingViewService.getPortMappings(deviceId, deviceInfo));
    }
}
