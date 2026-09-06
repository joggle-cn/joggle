package com.wuweibi.bullet.device.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDelDTO;
import com.wuweibi.bullet.device.domain.vo.MappingDeviceVO;
import com.wuweibi.bullet.device.service.DeviceMappingManagerService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.IpAddrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * <p>
 * 设备映射信息前端控制器
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Slf4j
@Tag(name = "设备映射")
@WebApi
@RestController
@RequestMapping("/api/user/device/mapping")
public class DeviceMappingController {

    @Resource
    private DeviceMappingService deviceMappingService;
    @Resource
    private WebsocketPool coonPool;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceMappingManagerService deviceMappingManagerService;

    @Operation(summary = "删除映射")
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public R delete(@RequestBody @Valid DeviceMappingDelDTO dto) {
        Long userId = SecurityUtils.getUserId();
        Long dmId = dto.getId();
        boolean status = deviceMappingService.exists(userId, dmId);
        if (status) {
            DeviceMapping entity = deviceMappingService.getById(dmId);
            deviceMappingService.removeById(dmId);

            DeviceDetail deviceDetail = deviceService.getDetail(entity.getDeviceId());
            if (deviceDetail == null) {
                return R.fail("设备不存在");
            }
            String deviceNo = deviceDetail.getDeviceNo();
            Integer serverTunnelId = deviceDetail.getServerTunnelId();
            JSONObject data = (JSONObject) JSON.toJSON(entity);
            MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
            coonPool.sendMessage(serverTunnelId, deviceNo, msg);
        }
        return R.success();
    }

    @GetMapping("/")
    public R<List<MappingDeviceVO>> device(@JwtUser Session session, @RequestParam Long deviceId) {
        DeviceDetail deviceDetail = deviceService.getDetail(deviceId);
        if (deviceDetail == null || !session.getUserId().equals(deviceDetail.getUserId())) {
            return R.fail("设备不存在");
        }
        return R.success(deviceMappingService.getByDeviceId(deviceId));
    }

    @Operation(summary = "更新映射信息")
    @Deprecated
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public R save(DeviceMapping deviceMapping) {
        Long userId = SecurityUtils.getUserId();
        deviceMapping.setUserId(userId);
        if (!IpAddrUtils.isIpv4(deviceMapping.getHost())) {
            return R.fail("请填写 IPv4 地址");
        }
        if (!IpAddrUtils.internalIp(deviceMapping.getHost())) {
            return R.fail("请填写内网 IP 地址");
        }

        DeviceMapping entity = deviceMappingService.getById(deviceMapping.getId());
        if (Objects.isNull(entity) || Objects.equals(entity.getProtocol(), DeviceMapping.PROTOCOL_KCP)) {
            return R.fail("映射不存在");
        }

        entity.setProtocol(deviceMapping.getProtocol());
        entity.setHostname(deviceMapping.getHostname());
        entity.setHost(deviceMapping.getHost());
        entity.setPort(deviceMapping.getPort());
        entity.setRemotePort(deviceMapping.getRemotePort());
        entity.setAuth(deviceMapping.getAuth());
        entity.setDescription(deviceMapping.getDescription());
        entity.setStatus(deviceMapping.getStatus());
        entity.setUserDomainId(deviceMapping.getUserDomainId());

        synchronized (userId) {
            return deviceMappingManagerService.saveOrUpdateMapping(userId, entity);
        }
    }
}
