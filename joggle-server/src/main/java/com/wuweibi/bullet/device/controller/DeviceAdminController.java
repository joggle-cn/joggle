package com.wuweibi.bullet.device.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.core.builder.MapBuilder;
import com.wuweibi.bullet.device.domain.dto.DeviceAdminParam;
import com.wuweibi.bullet.device.domain.dto.DeviceDelDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceSwitchLineDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceUpdateDTO;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceListVO;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.device.domain.vo.MappingDeviceVO;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgSwitchLine;
import com.wuweibi.bullet.protocol.MsgUnBind;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.StringUtil;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
@RequestMapping("/admin/device")
public class DeviceAdminController {

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
     * 设备分页查询
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("设备分页查询")
    @GetMapping("/list")
    public R<Page<DeviceListVO>> getAdminList(PageParam page, DeviceAdminParam params) {
        return R.ok(this.deviceService.getAdminList(page.toMybatisPlusPage(), params));
    }



    /**
     * 更新设备基本信息
     *
     * @return
     */
    @ApiOperation("更新设备信息")
    @PutMapping("/update")
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
    public R<Boolean> delete(@JwtUser Session session,
                         @RequestBody @Valid DeviceDelDTO dto,
                         HttpServletRequest request) {
        Long userId = session.getUserId();
        Long deviceId = dto.getId();

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, deviceId);
        if (status) {
            // 验证是否存在
            Device device = deviceService.getById(deviceId);
            DeviceOnline deviceOnline = deviceOnlineService.getOneByDeviceNo(device.getDeviceNo());
            if (deviceOnline == null) {
                return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
            }
            Bullet3Annotation bulletAnnotation = websocketPool.getByTunnelId(deviceOnline.getServerTunnelId());
            if(bulletAnnotation != null){
                MsgUnBind msg = new MsgUnBind();
                bulletAnnotation.sendMessage(device.getDeviceNo(), msg);
            }

            // 删除映射
            deviceMappingService.deleteByDeviceId(deviceId);

            deviceService.removeUserId(deviceId);
        }
        return R.ok();
    }






    /**
     * 获取设备信息
     *
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/detail")
    @ResponseBody
    public R device(@RequestParam Long deviceId) {
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }

        MapBuilder mapBuilder = newMap(3);
        // 设备信息
        DeviceDetailVO deviceInfo = deviceService.getDeviceInfoById(deviceId);
        if(deviceInfo == null){
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

        // 端口
        portList.forEach(item->{
            item.setDomain(deviceInfo.getServerAddr() + ":" + item.getRemotePort());
        });

        // 域名
        domainList.forEach(item -> {
            if (StringUtil.isNotBlank(item.getHostname())) {
                item.setDomain(item.getHostname());
            } else {
                item.setDomain(item.getDomain() + "." + deviceInfo.getServerAddr());
            }
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
        if(serverTunnel == null){
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



}
