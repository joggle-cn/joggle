package com.wuweibi.bullet.device.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.business.DeviceBiz;
import com.wuweibi.bullet.business.DomainBiz;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDelDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingProtocol;
import com.wuweibi.bullet.device.domain.dto.DeviceScanDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceServiceStatusDTO;
import com.wuweibi.bullet.device.domain.param.DeviceServiceParam;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceMappingClientVO;
import com.wuweibi.bullet.device.domain.vo.DeviceServiceVO;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.device.service.DeviceServiceService;
import com.wuweibi.bullet.device.service.DeviceMappingViewService;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.domain2.mapper.DomainMapper;
import com.wuweibi.bullet.domain2.service.UserDomainService;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgDeviceScan;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.protocol.consts.ProtocolType;
import com.wuweibi.bullet.protocol.consts.UserPackageLimitEnum;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.Objects;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * <p>
 *  设备映射信息 前端控制器
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Slf4j
@Tag(name = "设备服务")
@WebApi
@RestController
@RequestMapping("/api/user/device/service")
public class DeviceServiceController {


    /** 端口映射服务 */
    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource
    private WebsocketPool coonPool;


    /**
     * 删除映射关系
     * @return
     */
    @Operation(summary = "删除映射")
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Object delete(@RequestBody @Valid DeviceMappingDelDTO dto){
        Long userId = SecurityUtils.getUserId();
        Long dmId = dto.getId();
        // 验证设备映射是自己的
        boolean status = deviceMappingService.exists(userId, dmId);
        if (status) {
            DeviceMapping entity = deviceMappingService.getById(dmId);
            deviceMappingService.removeById(dmId);

            DeviceDetail deviceDetail = deviceService.getDetail(entity.getDeviceId());
            if (deviceDetail == null) {
                return R.fail("设备不存在");
            }
            String deviceNo = deviceDetail.getDeviceNo();
            JSONObject data = (JSONObject) JSON.toJSON(entity);
            MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
            coonPool.sendMessage(deviceDetail.getServerTunnelId(), deviceNo, msg);
        }
        return MessageFactory.getOperationSuccess();
    }

    @Resource
    private DeviceServiceService deviceServiceService;



    /**
     * 开始内网扫描
     * @param dto 参数
     * @return
     */
    @Operation(summary = "开始内网扫描")
    @PostMapping( "/start_scan")
    public R<Page<DeviceServiceVO>> startScan(@JwtUser Session session, @RequestBody DeviceScanDTO dto  ){
        String deviceNo = dto.getDeviceNo();
        Long userId = session.getUserId();

        Device device = deviceService.getByDeviceNo(deviceNo);
        if (Objects.isNull(device)) {
            return R.fail("设备不存在");
        }
        if (!userId.equals(device.getUserId())) {
            return R.fail("设备不是您的");
        }
        Integer serverTunnelId = device.getServerTunnelId();

        // 发送消息
        MsgDeviceScan msgDeviceScan = new MsgDeviceScan();
        coonPool.sendMessage(serverTunnelId, deviceNo, msgDeviceScan);

        return R.success();
    }

    /**
     * 关闭映射
     * @param dto 参数
     * @return
     */
    @Operation(summary = "关闭映射")
    @PostMapping( "/close_tunnel")
    public R<DeviceMappingClientVO> closeTunnel(@JwtUser Session session, @RequestBody DeviceServiceStatusDTO dto){
        Long mappingId = dto.getServiceId();
        Long userId = session.getUserId();
        DeviceMapping deviceMapping = deviceMappingService.getById(mappingId);
        if (Objects.isNull(deviceMapping)
                || !userId.equals(deviceMapping.getUserId())
                || Objects.equals(deviceMapping.getProtocol(), DeviceMapping.PROTOCOL_KCP)) {
            return R.fail("设备服务不存在");
        }
        Long deviceId = deviceMapping.getDeviceId();
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        Integer serverTunnelId = deviceInfo.getServerTunnelId();
        String deviceNo = deviceInfo.getDeviceNo();
        // 判断映射是否绑定域名 如果绑定则开启映射。
        if (deviceMapping.getDomainId() == null) {
            return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }
        // 判断映射的域名是否过期，过期后不允许开启
        if (!domainMapper.checkDoaminIdDue(userId, deviceMapping.getDomainId())) {
            return R.fail(SystemErrorType.DOMAIN_IS_DUE);
        }
        deviceMapping.setStatus(0);
        deviceMapping.setUpdateTime(new Date());
        deviceMappingService.updateById(deviceMapping);

        // 发送消息
        DeviceMappingProtocol deviceMappingProtocol = deviceMappingService.getMapping4ProtocolByMappingId(deviceMapping.getId());
        if (deviceMappingProtocol == null) {
            return R.fail("映射信息不存在");
        }

        JSONObject data = (JSONObject)JSON.toJSON(deviceMappingProtocol);
        Message msg = new MsgUnMapping(data.toJSONString());
        coonPool.sendMessage(serverTunnelId, deviceNo, msg);

        return R.success(deviceMappingViewService.getClientMapping(deviceMapping, deviceInfo));
    }

    @Resource
    private DomainBiz domainBiz;
    /**
     * 开启映射
     * @param dto 参数
     * @return
     */
    @Operation(summary = "开启映射")
    @PostMapping( "/open_tunnel")
    public R<DeviceMappingClientVO> openTunnel(@JwtUser Session session, @RequestBody DeviceServiceStatusDTO dto){
        Long mappingId = dto.getServiceId();
        Long userId = session.getUserId();
        DeviceMapping deviceMapping = deviceMappingService.getById(mappingId);
        if (Objects.isNull(deviceMapping)
                || !userId.equals(deviceMapping.getUserId())
                || Objects.equals(deviceMapping.getProtocol(), DeviceMapping.PROTOCOL_KCP)) {
            return R.fail("设备服务不存在");
        }
        Long deviceId = deviceMapping.getDeviceId();
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        Integer serverTunnelId = deviceInfo.getServerTunnelId();
        String deviceNo = deviceInfo.getDeviceNo();
        // 如果没有流量了，不能操作映射，会有一个缓冲过程
        if(!userFlowService.hasFlow(userId)){
            return R.fail(SystemErrorType.FLOW_IS_DUE);
        }

        // 判断映射是否绑定域名 如果绑定则开启映射。
        if (deviceMapping.getDomainId() != null) {
            // 判断映射的域名是否过期，过期后不允许开启
            if(!domainMapper.checkDoaminIdDue(userId, deviceMapping.getDomainId())){
                return R.fail(SystemErrorType.DOMAIN_IS_DUE);
            }
            deviceMapping.setStatus(1);
            deviceMapping.setUpdateTime(new Date());
        }else{
            // 自动查询可用的资源 并绑定到DeviceMapping
            Domain domain = domainService.getAvailableDomainByUserId(serverTunnelId, userId, deviceMapping.getPortProtocol());
            if (domain == null) {
                // 免费获取一个新域名
                R<Domain> domainR = domainBiz.getAvailableDomainByDeviceMapping(deviceMapping);
                if (domainR.isFail()) {
                    log.warn("用户{}套餐问题：{}", userId, domainR.getMsg());
                    return R.fail(SystemErrorType.DOMAIN_NOT_FOUND); // 引导用户去购买域名
                }
                domain = domainR.getData();
            }
            deviceMapping.setRemotePort(deviceMapping.getRemotePort());
            deviceMapping.setDomain(domain.getDomain());
            deviceMapping.setRemotePort(domain.getType()==1?Integer.parseInt(domain.getDomain()):null);
            deviceMapping.setStatus(1);
            deviceMapping.setDomainId(domain.getId());
            deviceMapping.setUpdateTime(new Date());
        }
        deviceMappingService.updateById(deviceMapping);

        // 发送消息
        DeviceMappingProtocol deviceMappingProtocol = deviceMappingService.getMapping4ProtocolByMappingId(deviceMapping.getId());
        if (deviceMappingProtocol == null) {
            return R.fail("映射信息不存在");
        }

        JSONObject data = (JSONObject)JSON.toJSON(deviceMappingProtocol);
        Message msg = new MsgMapping(data.toJSONString());
        coonPool.sendMessage(serverTunnelId, deviceNo, msg);

        return R.success(deviceMappingViewService.getClientMapping(deviceMapping, deviceInfo));
    }

    /**
     * 解除绑定公网域名（仅映射关闭状态下可执行）
     */
    @Operation(summary = "解除绑定公网域名")
    @PostMapping("/unbind_domain")
    public R unbindDomain(@JwtUser Session session, @RequestBody @Valid DeviceServiceStatusDTO dto) {
        Long mappingId = dto.getServiceId();
        Long userId = session.getUserId();
        DeviceMapping deviceMapping = deviceMappingService.getById(mappingId);
        if (Objects.isNull(deviceMapping)
                || !userId.equals(deviceMapping.getUserId())
                || Objects.equals(deviceMapping.getProtocol(), DeviceMapping.PROTOCOL_KCP)) {
            return R.fail("设备服务不存在");
        }
        if (!userId.equals(deviceMapping.getUserId())) {
            return R.fail("设备服务不属于您");
        }
        if (deviceMapping.getStatus() != 0) {
            return R.fail("请先关闭映射再解除域名绑定");
        }
        if (deviceMapping.getDomainId() == null) {
            return R.fail("该映射未绑定公网域名");
        }

        // 释放套餐使用次数
        UserPackageLimitEnum limitEnum = ProtocolType.toPackageEnum(deviceMapping.getProtocol());
        if (limitEnum != null) {
            userPackageManager.usePackageAdd(userId, limitEnum, -1);
        }

        // 清除域名绑定
        deviceMappingService.lambdaUpdate()
                .eq(DeviceMapping::getId, mappingId)
                .set(DeviceMapping::getDomainId, null)
                .set(DeviceMapping::getDomain, null)
                .set(DeviceMapping::getRemotePort, null)
                .set(DeviceMapping::getUpdateTime, new Date())
                .update();

        // 发送取消映射消息
        Integer serverTunnelId = deviceMapping.getServerTunnelId();
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceMapping.getDeviceId());
        if (deviceInfo != null) {
            String deviceNo = deviceInfo.getDeviceNo();
            DeviceMappingProtocol protocol = deviceMappingService.getMapping4ProtocolByMappingId(mappingId);
            if (protocol != null) {
                JSONObject data = (JSONObject) JSON.toJSON(protocol);
                Message msg = new MsgUnMapping(data.toJSONString());
                coonPool.sendMessage(serverTunnelId, deviceNo, msg);
            }
        }

        return R.success();
    }

    /**
     * 获取设备服务清单
     * @param params
     * @return
     */
    @Operation(summary = "获取设备服务清单")
    @GetMapping( "/list")
    public R<Page<DeviceServiceVO>> getDeviceServiceList(@JwtUser Session session, PageParam pageParams,DeviceServiceParam params){
        Long userId = session.getUserId();
        params.setUserId(userId);

        Page pageP = new Page<DeviceServiceVO>(pageParams.getCurrent(), pageParams.getSize());
        Page<DeviceServiceVO> page = deviceServiceService.getListPage(pageP, params);

        return R.success(page);
    }

    @Resource
    private DomainMapper domainMapper;
    @Resource
    private DomainService domainService;
    @Resource
    private UserFlowService userFlowService;
    @Resource
    private ServerTunnelService serverTunnelService;
    @Resource
    private UserPackageManager userPackageManager;


    @Resource
    private DeviceBiz deviceBiz;
    @Resource
    private UserDomainService userDomainService;
    @Resource
    private DeviceMappingViewService deviceMappingViewService;

    @Resource
    private DeviceService deviceService;



}
