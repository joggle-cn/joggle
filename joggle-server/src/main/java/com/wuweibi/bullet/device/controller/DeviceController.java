package com.wuweibi.bullet.device.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DevicePeersVO;
import com.wuweibi.bullet.device.domain.dto.*;
import com.wuweibi.bullet.device.domain.param.DeviceBindParam;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceInfoVO;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.DeviceMappingViewService;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.dto.DeviceDTO;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgCheckUpdate;
import com.wuweibi.bullet.protocol.MsgDeviceSecret;
import com.wuweibi.bullet.protocol.MsgSwitchLine;
import com.wuweibi.bullet.protocol.MsgUnBind;
import com.wuweibi.bullet.protocol.consts.UserPackageLimitEnum;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.res.service.UserPackageRightsService;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.HttpUtils;
import com.wuweibi.bullet.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
     * 支持下拉刷新（响应不缓存），支持按状态、os 筛选，按名称/延迟排序
     *
     * @return
     */
    @ApiOperation("用户的设备列表")
    @GetMapping
    public R<List<DeviceDTO>> device(DeviceWebQueryParam param) {
        Long userId = SecurityUtils.getUserId();
        List<DeviceDTO> list = deviceService.getWebListByUserId(userId, param);
        return R.ok(list);
    }

    /**
     * 最近设备列表（首页展示，按设备在线时间倒序）
     *
     * @return
     */
    @ApiOperation("首页最近设备")
    @GetMapping("/recent")
    public R<List<DeviceDTO>> recentDevice() {
        Long userId = SecurityUtils.getUserId();
        List<DeviceDTO> list = deviceService.getRecentWebListByUserId(userId, 2);
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

        boolean status = deviceService.exists(userId, deviceId);
        if (status) {
            deviceService.updateName(deviceId, name);
        }
        return R.success();
    }


    /**
     * 删除设备解绑
     *
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
        if (Objects.isNull(device)) {
            return R.fail("设备不存在");
        }
        if (!userId.equals(device.getUserId())) {
            return R.fail("您没有该设备权限");
        }

        DeviceOnline deviceOnline = deviceOnlineService.getOneByDeviceNo(device.getDeviceNo());
        if (deviceOnline == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
        }

        MsgUnBind msg = new MsgUnBind();
        websocketPool.sendMessage(deviceOnline.getServerTunnelId(), device.getDeviceNo(), msg);

        deviceMappingService.deleteByDeviceId(deviceId);
        deviceService.removeUserIdByDeviceNo(device.getDeviceNo());

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
     * 设备校验（绑定）
     *
     * @return
     */
    @ApiOperation("绑定设备")
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public R validate(@RequestBody DeviceBindParam param, HttpServletRequest request) {
        Long userId = SecurityUtils.getUserId();
        String deviceNo = param.getDeviceNo();
        if (StringUtil.isBlank(deviceNo)) {
            return R.fail(SystemErrorType.DEVICE_INPUT_NUMBER);
        }

        DeviceOnline deviceOnline = deviceOnlineService.getByDeviceNo(deviceNo);
        if (deviceOnline == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
        }

        Integer serverTunnelId = deviceOnline.getServerTunnelId();
        if (!userPackageManager.checkLimit(userId, UserPackageLimitEnum.DeviceNum, 1)) {
            return R.fail(SystemErrorType.DEVICE_BIND_LIMIT_ERROR);
        }

        Device device = deviceService.bindDevice(userId, deviceNo, serverTunnelId);
        userPackageManager.usePackageAdd(userId, UserPackageLimitEnum.DeviceNum, 1);

        MsgDeviceSecret msg = new MsgDeviceSecret();
        msg.setSecret(device.getDeviceSecret());
        websocketPool.sendMessage(serverTunnelId, deviceNo, msg);

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

    /**
     * 获取设备信息
     *
     * @param deviceId 设备 id
     * @return 设备详情
     */
    @ApiOperation("获取设备信息")
    @GetMapping(value = "/info")
    public R<DeviceInfoVO> deviceInfo(@RequestParam Long deviceId) {
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }

        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        if (deviceInfo == null) {
            return R.fail("设备不存在");
        }
        if (!userId.equals(deviceInfo.getUserId())) {
            return R.fail("用户设备不存在");
        }

        DeviceMappingViewService.MappingGroup mappingGroup = deviceMappingViewService.getMappingGroup(deviceId, deviceInfo);
        List<DevicePeersVO> p2pList = devicePeersService.getListByServerDeviceId(deviceId);

        DeviceInfoVO vo = new DeviceInfoVO();
        vo.setDeviceInfo(deviceInfo);
        vo.setPortList(mappingGroup.getPortList());
        vo.setDomainList(mappingGroup.getDomainList());
        vo.setP2pList(p2pList);

        DeviceInfoVO.Features features = new DeviceInfoVO.Features();
        features.setDomainCount(mappingGroup.getDomainList().size());
        features.setPortCount(mappingGroup.getPortList().size());
        features.setP2pCount(p2pList.size());
        vo.setFeatures(features);

        return R.ok(vo);
    }

    @Resource
    private DevicePeersService devicePeersService;
    @Resource
    private DeviceMappingViewService deviceMappingViewService;


    /**
     * 通过 mac 地址网络唤醒设备
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
    @GetMapping(value = "/discovery")
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

        boolean status = deviceService.exists(userId, deviceId);
        if (!status) {
            return R.fail("设备不存在");
        }

        ServerTunnel serverTunnel = serverTunnelService.getById(dto.getServerTunnelId());
        if (serverTunnel == null) {
            return R.fail("通道不存在");
        }

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

        MsgSwitchLine msg = new MsgSwitchLine();
        msg.setDeviceNo(deviceNo);

        String serverAddr = serverTunnel.getServerAddr();
        if (!(serverAddr.indexOf(":") > 0)) {
            serverAddr = serverAddr + ":8083";
        }
        msg.setServerAddr(serverAddr);
        websocketPool.sendMessage(deviceOnline.getServerTunnelId(), deviceNo, msg);

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

        Device device = deviceService.getById(deviceId);
        if (!Objects.equals(userId, device.getUserId())) {
            return R.fail("设备不存在");
        }
        String deviceNo = device.getDeviceNo();

        ServerTunnel serverTunnel = serverTunnelService.getById(device.getServerTunnelId());
        if (serverTunnel == null) {
            return R.fail("通道不存在");
        }

        MsgCheckUpdate msg = new MsgCheckUpdate();
        websocketPool.sendMessage(device.getServerTunnelId(), deviceNo, msg);
        return R.ok();
    }
}
