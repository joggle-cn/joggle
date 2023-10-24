package com.wuweibi.bullet.device.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.device.domain.dto.*;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.CodeHelper;
import com.wuweibi.bullet.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 数据收集(DataMetrics)表控制层
 *
 * @author marker
 * @since 2021-11-07 14:17:51
 */
@Slf4j
@WebApi
@Api(tags = "设备认证")
@RestController
@RequestMapping("/inner/open/device")
public class DeviceOpenInnerController {

    @Resource
    private DeviceService deviceService;


    @ApiOperation("设备秘钥校验【服务端调用校验】")
    @PostMapping(value = "/auth")
    public R<DeviceAuthVO> deviceSecret(@RequestBody @Valid DeviceAuthDTO deviceAuthDTO) {
        String deviceNo = deviceAuthDTO.getDeviceNo();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("deviceId", deviceNo);
        Device device = deviceService.getOne(queryWrapper);
        if (device != null && device.getUserId() != null) { // 设备存在且设备被绑定 强制校验秘钥
            String deviceSecret = device.getDeviceSecret();
            if (!deviceSecret.equals(deviceAuthDTO.getSecret())) { // 校验秘钥一致性
                return R.fail(SystemErrorType.DEVICE_SECRET_ERROR);
            }
        }
        // 其他情况认为设备是等待绑定状态
        DeviceAuthVO authVO = new DeviceAuthVO();
        if (StringUtil.isBlank(deviceNo)) {
            deviceNo = CodeHelper.makeNewCode();
        }
        authVO.setDeviceNo(deviceNo);
        authVO.setConcurrentNum(5);
        if(device != null){
            authVO.setConcurrentNum(device.getConcurrentNum());
        }


        // 设备在线
        DeviceOnlineInfoDTO deviceOnlineInfoDTO = new DeviceOnlineInfoDTO();
        deviceOnlineInfoDTO.setDeviceNo(deviceNo);
        deviceOnlineInfoDTO.setPublicIp(deviceAuthDTO.getRemoteIpAddr());
        deviceOnlineInfoDTO.setIntranetIp(deviceAuthDTO.getIpAddr());
        deviceOnlineInfoDTO.setMacAddr(deviceAuthDTO.getMac());
        deviceOnlineInfoDTO.setClientVersion(deviceAuthDTO.getVersion());
        deviceOnlineInfoDTO.setOs(deviceAuthDTO.getOs());
        deviceOnlineInfoDTO.setArch(deviceAuthDTO.getArch());
        deviceOnlineInfoDTO.setServerTunnelId(deviceAuthDTO.getServerTunnelId());
        deviceOnlineService.saveOrUpdate(deviceOnlineInfoDTO);
        return R.ok(authVO);
    }

    @Resource
    private DeviceOnlineService deviceOnlineService;
    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource
    private DomainService  domainService;

    @ApiOperation("获取映射扩展配置")
    @PostMapping(value = "/mapping/ext")
    public R<DomainConfigVO> deviceSecret(@RequestBody @Valid DeviceMappingConfigParam param) {
        Long mappingId = param.getMappingId();
        deviceMappingService.getMapping4ProtocolByMappingId(mappingId);
        Domain domain = domainService.getByMappingId(mappingId);
        DomainConfigVO domainConfigVO = new DomainConfigVO();
        domainConfigVO.setBandwidth(domain.getBandwidth());
        domainConfigVO.setConcurrentNum(domain.getConcurrentNum());
        return R.ok(domainConfigVO);
    }

}
