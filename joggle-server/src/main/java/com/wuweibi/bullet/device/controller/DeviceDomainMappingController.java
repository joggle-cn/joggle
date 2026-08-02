package com.wuweibi.bullet.device.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDomainDTO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingClientVO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.service.DeviceMappingManagerService;
import com.wuweibi.bullet.device.service.DeviceMappingViewService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "设备域名映射")
@WebApi
@RestController
@RequestMapping("/api/user/device/mapping/domain")
public class DeviceDomainMappingController {

    @Resource
    private DeviceMappingManagerService deviceMappingManagerService;
    @Resource
    private DeviceMappingViewService deviceMappingViewService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceMappingService deviceMappingService;

    @ApiOperation("保存或更新域名映射")
    @PostMapping("")
    public R save(@JwtUser Session session, @RequestBody @Valid DeviceMappingDomainDTO dto) {
        Long userId = session.getUserId();
        synchronized (userId) {
            return deviceMappingManagerService.saveDomainMapping(userId, dto);
        }
    }

    @ApiOperation("域名映射列表")
    @GetMapping("/list")
    public R<List<DeviceMappingClientVO>> list(@JwtUser Session session, @RequestParam Long deviceId) {
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        if (deviceInfo == null) {
            return R.fail("设备不存在");
        }
        if (!session.getUserId().equals(deviceInfo.getUserId())) {
            return R.fail("用户设备不存在");
        }
        return R.success(deviceMappingViewService.getDomainMappings(deviceId, deviceInfo));
    }

    @ApiOperation("映射详情")
    @GetMapping("/detail")
    public R<DeviceMappingDetailVO> mappingDomainDetail(@JwtUser Session session, @RequestParam Long mappingId) {
        Long userId = session.getUserId();
        if (!deviceMappingService.exists(userId, mappingId)) {
            return R.fail("映射不存在");
        }
        DeviceMapping mapping = deviceMappingService.getById(mappingId);
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(mapping.getDeviceId());
        if (deviceInfo == null) {
            return R.fail("设备不存在");
        }
        DeviceMappingDetailVO vo = new DeviceMappingDetailVO();
        BeanUtils.copyProperties(deviceMappingViewService.getClientMapping(mapping, deviceInfo), vo);
        return R.success(vo);
    }
}
