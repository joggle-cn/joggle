package com.wuweibi.bullet.device.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.core.builder.MapBuilder;
import com.wuweibi.bullet.device.domain.dto.DeviceDelDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceSwitchLineDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceUpdateDTO;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.protocol.MsgDeviceSecret;
import com.wuweibi.bullet.protocol.MsgSwitchLine;
import com.wuweibi.bullet.protocol.MsgUnBind;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.HttpUtils;
import com.wuweibi.bullet.utils.StringUtil;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static com.wuweibi.bullet.core.builder.MapBuilder.newMap;

/**
 * 设备：提供设备的管理功能，能够对设备绑定、查询、设备解绑、设备信息更新等功能。
 *
 * @author marker
 * @create 2017-12-06 下午9:19
 **/
@Slf4j
@RestController
@RequestMapping("/api/user/device")
public class DeviceController {


    @Resource
    private CoonPool coonPool;


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
    @GetMapping
    public Object device(@JwtUser Session session) {

        Long userId = session.getUserId();

        List<Device> list = deviceService.listByMap(newMap(1)
                .setParam("userId", userId)
                .build());

        Iterator<Device> it = list.iterator();

        List<DeviceDto> deviceList = new ArrayList<>();

        while (it.hasNext()) {
            Device device = it.next();

            DeviceDto deviceDto = new DeviceDto(device);
            String deviceNo = device.getDeviceNo();

            int status = getStatus(deviceNo);
            deviceDto.setStatus(status);
            // TODO 性能问题
            DeviceOnline deviceOnline = deviceOnlineService.selectByDeviceNo(deviceNo);
            if (deviceOnline != null) {
                deviceDto.setIntranetIp(deviceOnline.getIntranetIp());
                deviceDto.setOnlineTime(deviceOnline.getUpdateTime());
            }
            deviceList.add(deviceDto);
        }
        return MessageFactory.get(deviceList);
    }


    /**
     * 获取设备状态
     *
     * @param deviceCode
     * @return
     */
    private int getStatus(String deviceCode) {
        return coonPool.getDeviceStatus(deviceCode);
    }


    /**
     * 更新设备基本信息
     *
     * @return
     */
    @PostMapping()
    public R save(@JwtUser Session session,
                  @RequestBody @Valid DeviceUpdateDTO dto) {
        Long userId = session.getUserId();
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
     * 删除设备
     * @return
     */
    @DeleteMapping(value = "")
    public Object delete(@JwtUser Session session,
                         @RequestBody @Valid DeviceDelDTO dto,
                         HttpServletRequest request) {
        Long userId = session.getUserId();
        Long dId = dto.getId();

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, dId);
        if (status) {
            Device device = deviceService.getById(dId);

            // 删除映射
            deviceMappingService.deleteByDeviceId(dId);
            deviceService.removeById(dId);

            try {
                BulletAnnotation bulletAnnotation = coonPool.getByDeviceNo(device.getDeviceNo());

                MsgUnBind msg = new MsgUnBind();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                msg.write(outputStream);
                // 包装了Bullet协议的
                byte[] resultBytes = outputStream.toByteArray();
                ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                bulletAnnotation.getSession().getBasicRemote().sendBinary(buf);
                // 停止ws链接
                bulletAnnotation.stop();
            } catch (Exception e) {
                log.error("{}", e.getMessage());
            }

        }
        return MessageFactory.getOperationSuccess();
    }


    /**
     * 设备校验(绑定)
     *
     * @return
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public R validate(@JwtUser Session session, String deviceId, HttpServletRequest request) {
        Long userId = session.getUserId();

        // 没有输入设备ID
        if (StringUtils.isEmpty(deviceId)) {
            return R.fail(SystemErrorType.DEVICE_INPUT_NUMBER);
        }

        // 验证是否存在
        DeviceOnline deviceOnline = deviceOnlineService.getByDeviceNo(deviceId);
        if (deviceOnline == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
        }
        // 验证是否绑定
        boolean isBind = deviceService.existsDevice(deviceId);
        if (isBind) {
            return R.fail(SystemErrorType.DEVICE_OTHER_BIND);
        }
        // 限制普通用户绑定设备的数量10 排除自己的账号判断
        int deviceNum = deviceService.getCountByUserId(userId);
        if (deviceNum >= 10 && userId != 1) {
            return R.fail(SystemErrorType.DEVICE_BIND_LIMIT_ERROR);
        }

        // 给当前用户存储最新的设备数据
        Device device = new Device();
        device.setDeviceNo(deviceId);
        device.setUserId(userId);
        device.setCreateTime(new Date());
        device.setName(deviceId);
        device.setMacAddr(deviceOnline.getMacAddr());
        device.setIntranetIp(deviceOnline.getIntranetIp());

        // 生成设备秘钥
        String deviceSecret = Md5Crypt.md5Crypt(deviceId.getBytes(), null, "");
        device.setDeviceSecret(deviceSecret);

        deviceService.save(device);
        // 发送消息通知设备秘钥

        BulletAnnotation annotation = coonPool.getByDeviceNo(deviceId);
        if (annotation != null) {
            MsgDeviceSecret msg = new MsgDeviceSecret();
            msg.setSecret(deviceSecret);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                msg.write(outputStream);
                // 包装了Bullet协议的
                byte[] resultBytes = outputStream.toByteArray();
                ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                annotation.getSession().getBasicRemote().sendBinary(buf);

            } catch (IOException e) {
                log.error("", e);
            } finally {
                IOUtils.closeQuietly(outputStream);
            }
        }
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
     * @param request
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/info")
    @ResponseBody
    public Object device(HttpServletRequest request, @RequestParam Long deviceId,
                         @JwtUser Session session) {
        if (session.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }
        Long userId = session.getUserId();


        MapBuilder mapBuilder = newMap(3);

        // 设备信息
        JSONObject deviceInfo = deviceService.getDeviceInfoById(deviceId);
//        JSONObject deviceInfo = (JSONObject) JSON.toJSON(device);

        Long deviceUserId = deviceInfo.getLong("userId");
        String deviceNo = deviceInfo.getString("deviceNo");
        String serverAddr = deviceInfo.getString("serverAddr"); // 通道顶级地址

        if (!deviceUserId.equals(userId)) {
            return R.fail("设备不存在");
        }

        DeviceOnline deviceOnline = deviceOnlineService.selectByDeviceNo(deviceNo);

        if (deviceOnline != null) {
            deviceInfo.put("intranetIp", deviceOnline.getIntranetIp());
            deviceInfo.put("clientVersion", deviceOnline.getClientVersion());
            deviceInfo.put("status", deviceOnline.getStatus());
        } else {
            deviceInfo.put("clientVersion", "");
            deviceInfo.put("status", -1);
        }

        // 端口
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userId", userId);
        wrapper.eq("device_id", deviceId);
        wrapper.in("protocol", 2,5);

        List<DeviceMapping> portList = deviceMappingService.list(wrapper);
        portList.forEach(item->{
            item.setDomain(serverAddr+":"+item.getRemotePort());
        });

        // 域名
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("userId", userId);
        wrapper2.eq("device_id", deviceId);
        wrapper2.in("protocol", Arrays.asList(1, 3, 4));

        List<DeviceMapping> domainList = deviceMappingService.list(wrapper2);
        domainList.forEach(item -> {
            if (StringUtil.isNotBlank(item.getHostname())) {
                item.setDomain(item.getHostname());
            } else {
                item.setDomain(item.getDomain() + "." + serverAddr);
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

        return MessageFactory.get(mapBuilder.build());
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
    @RequestMapping(value = "/discovery", method = RequestMethod.GET)
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
        if(serverTunnel == null){
            return R.fail("通道不存在");
        }

        // 检查设备是否有映射
        if (deviceMappingService.countByDeviceId(deviceId) > 0) {
            return R.fail("存在映射，不支持切换通道");
        }

        Device device = deviceService.getById(deviceId);
        String deviceNo = device.getDeviceNo();
        device.setServerTunnelId(dto.getServerTunnelId());
        deviceService.updateById(device);

        // 发送切换消息给设备
        BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
        if (annotation != null) {
            MsgSwitchLine msg = new MsgSwitchLine();
            msg.setDeviceNo(deviceNo);
            msg.setServerAddr(serverTunnel.getServerAddr());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                msg.write(outputStream);
                // 包装了Bullet协议的
                byte[] resultBytes = outputStream.toByteArray();
                ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                annotation.getSession().getBasicRemote().sendBinary(buf);
            } catch (IOException e) {
                log.error("", e);
            } finally {
                IOUtils.closeQuietly(outputStream);
            }
        }

        return R.success();
    }


}
