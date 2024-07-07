package com.wuweibi.bullet.device.controller;
/**
 * Created by marker on 2017/12/6.
 */

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.config.cache.RedisTemplateConfig;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.core.builder.MapBuilder;
import com.wuweibi.bullet.device.domain.dto.DeviceCheckUpdateDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceDelDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceSwitchLineDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceUpdateDTO;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.device.domain.vo.MappingDeviceVO;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.enums.ProtocolTypeEnum;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgCheckUpdate;
import com.wuweibi.bullet.protocol.MsgDeviceSecret;
import com.wuweibi.bullet.protocol.MsgSwitchLine;
import com.wuweibi.bullet.protocol.MsgUnBind;
import com.wuweibi.bullet.res.manager.UserPackageLimitEnum;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.res.service.UserPackageRightsService;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.HttpUtils;
import com.wuweibi.bullet.utils.StringUtil;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.wuweibi.bullet.alias.CacheCode.DEVICE_MAPPING_STATISTICS_FLOW_TODAY;
import static com.wuweibi.bullet.alias.CacheCode.DEVICE_MAPPING_STATISTICS_LINK_TODAY;
import static com.wuweibi.bullet.core.builder.MapBuilder.newMap;

/**
 * 设备：提供设备的管理功能，能够对设备绑定、查询、设备解绑、设备信息更新等功能。
 *
 * @author marker
 * @create 2017-12-06 下午9:19
 **/
@Slf4j
@WebApi
@Api(tags = "设备管理")
@RestController
@RequestMapping("/api/user/device")
public class DeviceController {

    @Resource
    private WebsocketPool websocketPool;

    /**
     * 设备管理
     */
    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceOnlineService deviceOnlineService;

    @Resource
    private DeviceMappingService deviceMappingService;

    /**
     * 设备列表
     *
     * @return
     */
    @ApiOperation("设备下拉列表")
    @GetMapping("/options")
    public R<List<DeviceOption>> deviceOptions() {
        Long userId = SecurityUtils.getUserId();
        List<DeviceOption> list = deviceService.getOptionListByUserId(userId);
        return R.ok(list);
    }

    /**
     * 设备列表
     *
     * @return
     */
    @ApiOperation("用户的设备列表")
    @GetMapping
    public R<List<DeviceDto>> device() {
        Long userId = SecurityUtils.getUserId();
        List<DeviceDto> list = deviceService.getWebListByUserId(userId);
        return R.ok(list);
    }


    /**
     * 更新设备基本信息
     *
     * @return
     */
    @ApiOperation("更新设备信息")
    @PostMapping()
    public R save(@RequestBody @Valid DeviceUpdateDTO dto) {
        Long userId = SecurityUtils.getUserId();
        Long deviceId = dto.getId();
        String name = dto.getName();

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, deviceId);
        if (status) {
            deviceService.updateName(deviceId, name);
        }
        return R.success();
    }


    /**
     * 删除设备 解绑
     * @return
     */
    @ApiOperation("删除设备")
    @DeleteMapping(value = "")
    @Transactional
    public R<Boolean> delete(@JwtUser Session session,
                         @RequestBody @Valid DeviceDelDTO dto,
                         HttpServletRequest request) {
        Long userId = session.getUserId();
        Long deviceId = dto.getId();

        Device device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) { // 验证是否存在
            return R.fail("设备不存在");
        }
        if (!userId.equals(device.getUserId())) { // 校验设备是否是他的
            return R.fail("您没有该设备权限");
        }

        DeviceOnline deviceOnline = deviceOnlineService.getOneByDeviceNo(device.getDeviceNo());
        if (deviceOnline == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
        }
        Bullet3Annotation bulletAnnotation = websocketPool.getByTunnelId(deviceOnline.getServerTunnelId());
        if(bulletAnnotation != null){
            MsgUnBind msg = new MsgUnBind();
            bulletAnnotation.sendMessage(device.getDeviceNo(), msg);
        }

        deviceMappingService.deleteByDeviceId(deviceId); // 删除映射
        deviceService.removeUserIdByDeviceNo(device.getDeviceNo()); // 清理用户归属

        // 设备移除，权益移除
        R r1 = userPackageManager.usePackageAdd(userId, UserPackageLimitEnum.DeviceNum, -1);
        if (r1.isFail()) {
            throw new RException(r1);
        }
        return R.ok();
    }
    @Resource
    private UserPackageRightsService userPackageRightsService;

    @Resource
    private UserPackageManager userPackageManager;

    /**
     * 设备校验(绑定)
     *
     * @return
     */
    @ApiOperation("绑定设备")
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public R validate(String deviceId, HttpServletRequest request) {
        Long userId = SecurityUtils.getUserId();
        String deviceNo = deviceId;
        // 没有输入设备ID
        if (StringUtil.isBlank(deviceNo)) {
            return R.fail(SystemErrorType.DEVICE_INPUT_NUMBER);
        }

        // 验证是否存在
        DeviceOnline deviceOnline = deviceOnlineService.getByDeviceNo(deviceNo);
        if (deviceOnline == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
        }

        Bullet3Annotation annotation = websocketPool.getByTunnelId(deviceOnline.getServerTunnelId());
        if (annotation == null) {
            return R.fail("ngrokd实例不在线, 请联系管理员");
        }

        // 套餐设备数量限制校验
        if (!userPackageManager.checkLimit(userId, UserPackageLimitEnum.DeviceNum, 1)) {
            return R.fail(SystemErrorType.DEVICE_BIND_LIMIT_ERROR);
        }
        Device device = deviceService.bindDevice(userId, deviceNo);
        userPackageManager.usePackageAdd(userId, UserPackageLimitEnum.DeviceNum, 1);

        // 发送消息通知设备秘钥
        MsgDeviceSecret msg = new MsgDeviceSecret();
        msg.setSecret(device.getDeviceSecret());
        annotation.sendMessage(deviceNo, msg);

        return R.success();
    }


    @Deprecated
    @RequestMapping(value = "/uuid", method = RequestMethod.GET)
    @ResponseBody
    public Map uuid(HttpServletRequest request) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        JSONObject result = new JSONObject();
        result.put("uuid", uuid);
        return result;
    }



    @Resource(name = RedisTemplateConfig.BEAN_REDIS_TEMPLATE)
    private RedisTemplate<String, Object> redisTemplate;



    /**
     * 获取设备信息
     *
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/info")
    @ResponseBody
    public R device(@RequestParam Long deviceId) {
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }

        MapBuilder mapBuilder = newMap(3);
        // 设备信息
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        if (deviceInfo == null) {
            return R.fail("设备不存在");
        }
        if (!userId.equals(deviceInfo.getUserId())) {
            return R.fail("用户设备不存在");
        }

        List<MappingDeviceVO> mappingList = deviceMappingService.getByDeviceId(deviceId);

        List<MappingDeviceVO> portList = mappingList.stream()
                .filter(item-> ArrayUtils.contains(new Integer[]{2, 5},item.getProtocol()))
                .collect(Collectors.toList());
        List<MappingDeviceVO> domainList = mappingList.stream()
                .filter(item-> ArrayUtils.contains(new Integer[]{1, 3, 4},item.getProtocol()))
                .collect(Collectors.toList());


        String date = DateUtil.format(new Date(), "yyyyMMdd");
        String keyBytes = String.format(DEVICE_MAPPING_STATISTICS_FLOW_TODAY, date);
        String keyLink = String.format(DEVICE_MAPPING_STATISTICS_LINK_TODAY, date);
        BoundHashOperations<String, Object, Object> keyBytesMap = redisTemplate.boundHashOps(keyBytes);
        BoundHashOperations<String, Object, Object> keyLinkMap = redisTemplate.boundHashOps(keyLink);


        // 端口
        portList.forEach(item->{
            String protocol = ProtocolTypeEnum.getProtocol(item.getProtocol());
            item.setDomain(deviceInfo.getServerAddr() + ":" + item.getRemotePort());
            String domain = item.getDomain();
            if (StringUtil.isNotBlank(item.getHostname())) {
                domain = item.getHostname();
            }
            item.setUrl(String.format("%s://%s", protocol, domain));
            // 今日流量 & 链接数
            Integer flowKb = (Integer) keyBytesMap.get(item.getId().toString());
            Integer linkNum = (Integer) keyLinkMap.get(item.getId().toString());
            item.setTodayFlow(new BigDecimal(flowKb == null ? 0 : flowKb).divide(BigDecimal.valueOf(1024)));
            item.setLink(linkNum == null ? 0 : linkNum);

        });

        // 域名
        domainList.forEach(item -> {
            String protocol = ProtocolTypeEnum.getProtocol(item.getProtocol());
            item.setDomain(item.getDomain() + "." + deviceInfo.getServerAddr());
            String domain = item.getDomain();
            if (StringUtil.isNotBlank(item.getHostname())) {
                domain = item.getHostname();
            }
            item.setUrl(String.format("%s://%s", protocol, domain));
            // 今日流量 & 链接数
            Integer flowKb = (Integer) keyBytesMap.get(item.getId().toString());
            Integer linkNum = (Integer) keyLinkMap.get(item.getId().toString());
            item.setTodayFlow(new BigDecimal(flowKb == null? 0: flowKb).divide(BigDecimal.valueOf(1024)));
            item.setLink(linkNum == null ? 0 : linkNum);
        });

        mapBuilder
                .setParam("deviceInfo", deviceInfo);
        mapBuilder.setParam("features", newMap(4)
                .setParam("domainCount", domainList.size())
                .setParam("portCount", portList.size())
                .build());

        // 端口
        mapBuilder.setParam("portList", portList);
        // 域名
        mapBuilder.setParam("domainList", domainList);

        return R.ok(mapBuilder.build());
    }


    /**
     * 通过mac地址网络唤醒设备
     *
     * @return
     */
    @RequestMapping(value = "/wol", method = RequestMethod.POST)
    @ResponseBody
    public R WOL(@JwtUser Session session, String mac) {
        deviceService.wakeUp(session.getUserId(), mac);
        return R.success();
    }


    /**
     * 设备发现接口
     *
     * @return
     */
    @GetMapping(value = "/discovery" )
    public R discovery(HttpServletRequest request) {
        String ip = HttpUtils.getRemoteIP(request);
        List<DeviceOnline> list = deviceService.getDiscoveryDevice(ip);
        return R.success(list);
    }



    @Resource
    private ServerTunnelService serverTunnelService;


    /**
     * 设备切换线路
     *
     * @return
     */
    @PostMapping("/switch-line")
    public R<Boolean> switchLine(@JwtUser Session session,
                  @RequestBody @Valid DeviceSwitchLineDTO dto) {
        Long userId = session.getUserId();
        Long deviceId = dto.getDeviceId();

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, deviceId);
        if (!status) {
            return R.fail("设备不存在");
        }

        ServerTunnel serverTunnel = serverTunnelService.getById(dto.getServerTunnelId());
        if (serverTunnel == null) {
            return R.fail("通道不存在");
        }

        // 检查设备是否有映射
        if (deviceMappingService.countByDeviceId(deviceId) > 0) {
            return R.fail("存在映射，不支持切换通道");
        }

        Device device = deviceService.getById(deviceId);
        String deviceNo = device.getDeviceNo();
        deviceService.updateById(device);

        DeviceOnline deviceOnline = deviceOnlineService.getByDeviceNo(deviceNo);
        if (deviceOnline == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
        }

        // 发送切换消息给设备
        Bullet3Annotation annotation = websocketPool.getByTunnelId(deviceOnline.getServerTunnelId());
        if (annotation != null) {
            MsgSwitchLine msg = new MsgSwitchLine();
            msg.setDeviceNo(deviceNo);

            String serverAddr = serverTunnel.getServerAddr();
            if (!(serverAddr.indexOf(":") > 0)) {
                serverAddr = serverAddr + ":8083";
            }
            msg.setServerAddr(serverAddr);
            annotation.sendMessage(deviceNo,  msg);
        }

        return R.success();
    }



    /**
     * 检查更新接口
     *
     * @return
     */
    @ApiOperation("触发设备检查更新")
    @PostMapping("/check-update")
    public R<Boolean> checkUpdate(@JwtUser Session session,
                                 @RequestBody @Valid DeviceCheckUpdateDTO dto) {
        Long userId = session.getUserId();
        Long deviceId = dto.getDeviceId();


        // 校验设备是否是他的
        Device device = deviceService.getById(deviceId);
        if (!Objects.equals(userId, device.getUserId())){
            return R.fail("设备不存在");
        }
        String deviceNo = device.getDeviceNo();

        ServerTunnel serverTunnel = serverTunnelService.getById(device.getServerTunnelId());
        if (serverTunnel == null){
            return R.fail("通道不存在");
        }

        // 发送切换消息给设备
        Bullet3Annotation annotation = websocketPool.getByTunnelId(device.getServerTunnelId());
        if (annotation == null) {
            return R.fail("通道不在线");
        }
        MsgCheckUpdate msg = new MsgCheckUpdate();
        annotation.sendMessage(deviceNo,  msg);
        return R.ok();
    }



}
