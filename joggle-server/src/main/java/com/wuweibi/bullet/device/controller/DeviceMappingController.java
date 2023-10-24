package com.wuweibi.bullet.device.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.business.DeviceBiz;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDelDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingProtocol;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain2.entity.UserDomain;
import com.wuweibi.bullet.domain2.mapper.DomainMapper;
import com.wuweibi.bullet.domain2.service.UserDomainService;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.IpAddrUtils;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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
@Api(tags = "设备映射")
@WebApi
@RestController
@RequestMapping("/api/user/device/mapping")
public class DeviceMappingController {


    /** 端口映射服务 */
    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource
    private WebsocketPool coonPool;


    /**
     * 删除映射关系
     * @return
     */
    @ApiOperation("删除映射")
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
            Bullet3Annotation annotation = coonPool.getByTunnelId(deviceDetail.getServerTunnelId());
            if (annotation != null) {
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
                annotation.sendMessage(deviceNo, msg);
            }
        }
        return MessageFactory.getOperationSuccess();
    }

    /**
     * 获取映射详情
     * @param deviceId
     * @return
     */
    @GetMapping( "/")
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
    @Resource
    private ServerTunnelService serverTunnelService;


    @Resource
    private DeviceBiz deviceBiz;
    @Resource
    private UserDomainService userDomainService;

    /**
     * 保存或者更新数据
     * @param deviceMapping
     * @return
     */
    @ApiOperation("更新映射信息")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public R save(DeviceMapping deviceMapping ){
        Long userId = SecurityUtils.getUserId();
        deviceMapping.setUserId(userId);
        if (!IpAddrUtils.isIpv4(deviceMapping.getHost())) {
            return R.fail("请填写ipV4地址");
        }
        // 判断ip是内网ip
        if (!IpAddrUtils.internalIp(deviceMapping.getHost())) {
            return R.fail("请填写内网ip地址");
        }

        DeviceMapping entity = deviceMappingService.getById(deviceMapping.getId());
//        entity.setDomainId(deviceMapping.getDomainId());
//        entity.setDomain(deviceMapping.getDomain());
        entity.setProtocol(deviceMapping.getProtocol());
        entity.setHostname(deviceMapping.getHostname());
        entity.setHost(deviceMapping.getHost());
        entity.setPort(deviceMapping.getPort());
        entity.setRemotePort(deviceMapping.getRemotePort());
        entity.setAuth(deviceMapping.getAuth());
        entity.setDescription(deviceMapping.getDescription());
        entity.setStatus(deviceMapping.getStatus());
        entity.setUserDomainId(deviceMapping.getUserDomainId());

        // 验证设备映射是自己的
        if(!deviceMappingService.exists(userId, entity.getId())){
            return R.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
        }
        // 判断映射的域名是否过期，过期后不允许开启
        if(!domainMapper.checkDoaminIdDue(userId, entity.getDomainId())){
            if(entity.getStatus() == 1){ // 不能启用
                return R.fail(SystemErrorType.DOMAIN_IS_DUE);
            }
        }

        // 如果没有流量了，不能操作映射，会有一个缓冲过程
        if(!userFlowService.hasFlow(userId)){
            return R.fail(SystemErrorType.FLOW_IS_DUE);
        }

//        if(StringUtil.isNotBlank(entity.getHostname())){
//            String baseDomain = StringUtil.getBaseDomain(entity.getHostname());
//            if(this.serverTunnelService.checkDomain(baseDomain)){
//                return R.fail("自定义域名不支持配置官方域名");
//            }
//        }
        // 查询自定义域名信息
        entity.setHostname("");
        if (Objects.nonNull(entity.getUserDomainId())) {
            UserDomain userDomain = userDomainService.getById(entity.getUserDomainId());
            if(!userId.equals(userDomain.getUserId())){
                return R.fail("用户域名id不存在");
            }
            entity.setHostname(userDomain.getDomain());

            // 校验用户域名是否绑定其他映射
            if (deviceMappingService.checkUserDomain(entity.getId(), entity.getUserDomainId())) {
                return R.fail("用户域名已绑定其他映射");
            }
        }
        if (entity.getId() != null){
            deviceMappingService.updateById(entity);
        } else {
            // 验证域名是否被使用
            boolean isOk = deviceMappingService.existsDomain(entity.getDomain());
            if(isOk){
                return R.fail(SystemErrorType.DOMAIN_IS_OTHER_BIND);
            }
            deviceMappingService.save(entity);
        }

        DeviceDetail deviceDetail = deviceService.getDetail(entity.getDeviceId());
        if (deviceDetail == null) {
            return R.fail("设备不存在");
        }
        String deviceNo = deviceDetail.getDeviceNo();

        Bullet3Annotation annotation = coonPool.getByTunnelId(deviceDetail.getServerTunnelId());
        if (annotation == null) {// 设备不在线
            return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
        }
        DeviceMappingProtocol deviceMappingProtocol = deviceMappingService.getMapping4ProtocolByMappingId(entity.getId());
        if (deviceMappingProtocol == null) {
            return R.fail("映射信息不存在");
        }

        JSONObject data = (JSONObject)JSON.toJSON(deviceMappingProtocol);
        Message msg;
        if (entity.getStatus() == 1) { // 启用映射
            msg = new MsgMapping(data.toJSONString());
        } else {
            log.debug("设备 {} 停用 {} 映射", entity.getDeviceId(), entity.getId());
            msg = new MsgUnMapping(data.toJSONString());
        }
        annotation.sendMessage(deviceNo, msg);

        return R.success();
    }

    @Resource
    private DeviceService deviceService;



}
