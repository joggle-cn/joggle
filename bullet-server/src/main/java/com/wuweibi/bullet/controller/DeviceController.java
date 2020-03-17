package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.core.builder.MapBuilder;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.wuweibi.bullet.core.builder.MapBuilder.newMap;
import static com.wuweibi.bullet.utils.SessionHelper.getUserId;

/**
 *
 *
 * @author marker
 * @create 2017-12-06 下午9:19
 **/
@Slf4j
@RestController
@RequestMapping("/api/user")
public class DeviceController {



    @Resource
    private CoonPool coonPool;


    /** 设备管理 */
    @Resource
    private DeviceService deviceService;


    @Resource
    private DeviceOnlineService deviceOnlineService;

    @Resource
    private DeviceMappingService deviceMappingService;


    /**
     * 设备列表
     * @return
     */
    @RequestMapping(value = "/device/", method = RequestMethod.GET)
    public Object device(@JwtUser Session session){

        Long userId = session.getUserId();

        List<Device> list  =deviceService.listByMap(newMap(1)
                .setParam("userId", userId)
                .build());

        Iterator<Device> it = list.iterator();

        List<DeviceDto> deviceList = new ArrayList<>();

        while (it.hasNext()){
            Device device = it.next();

            DeviceDto deviceDto = new DeviceDto(device);
            String deviceNo = device.getDeviceNo();

            int status = getStatus(deviceNo);
            deviceDto.setStatus(status);

           DeviceOnline deviceOnline =  deviceOnlineService.selectByDeviceNo( deviceNo);
           if(deviceOnline != null){
               deviceDto.setIntranetIp(deviceOnline.getIntranetIp());
               deviceDto.setOnlineTime(deviceOnline.getUpdateTime());
           }
            deviceList.add(deviceDto);
        }
        return MessageFactory.get(deviceList);
    }


    /**
     * 获取设备状态
     * @param deviceCode
     * @return
     */
    private int getStatus(String deviceCode){
        return coonPool.getDeviceStatus(deviceCode);
    }


    // 更新设备
    @RequestMapping(value = "/device/", method = RequestMethod.POST)
    public Object save(@RequestParam String name,
                       @RequestParam Long id,
                       HttpServletRequest request ){
        Long userId = getUserId(request);

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, id);
        if(status){
            deviceService.updateName(id, name);
        }
        return MessageFactory.getOperationSuccess();
    }


    /**
     * 删除设备
     * @return
     */
    @RequestMapping(value = "/device/", method = RequestMethod.DELETE)
    public Object save(@JwtUser Session session,
                       @RequestParam Long id,
                       HttpServletRequest request ){
        Long userId = session.getUserId();

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, id);
        if(status){
            Device device = deviceService.getById(id);

            // 删除映射
            deviceMappingService.deleteByDeviceId(id);
            deviceService.removeById(id);

            try {
                // 停止ws链接
                BulletAnnotation bulletAnnotation = coonPool.getByDeviceNo(device.getDeviceNo());
                bulletAnnotation.stop();
            } catch (Exception e){
                log.error("{}", e.getMessage());
            }

        }
        return MessageFactory.getOperationSuccess();
    }


    /**
     * 设备校验
     * @return
     */
    @RequestMapping(value = "/device/validate", method = RequestMethod.GET)
    @ResponseBody
    public Result validate(@JwtUser Session session, String deviceId,  HttpServletRequest request){
        Long userId = session.getUserId();

        // 没有输入设备ID
        if(StringUtils.isEmpty(deviceId)){
            return Result.fail(SystemErrorType.DEVICE_INPUT_NUMBER);
        }

        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("deviceNo", deviceId);
        wrapper2.eq("status", 1);
        wrapper2.orderByDesc("updateTime");

        // 验证是否存在
        DeviceOnline deviceOnline = deviceOnlineService.getOne(wrapper2);
        if(deviceOnline != null){
            // 验证是否绑定
            boolean isBinded = deviceService.existsDevice(deviceId);
            if(isBinded){
                return Result.fail(SystemErrorType.DEVICE_OTHER_BIND);
            }

            // 给当前用户存储最新的设备数据
            Device device = new Device();
            device.setDeviceNo(deviceId);
            device.setUserId(userId);
            device.setCreateTime(new Date());
            device.setName("default");
            device.setMacAddr(deviceOnline.getMacAddr());
            device.setIntranetIp(deviceOnline.getIntranetIp());

            deviceService.save(device);

            return Result.success();
        }
        return Result.fail(SystemErrorType.DEVICE_NOT_ONLINE);
    }


    @RequestMapping(value = "/uuid", method = RequestMethod.GET)
    @ResponseBody
    public Map uuid(HttpServletRequest request ){

        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        JSONObject result = new JSONObject();
        result.put("uuid", uuid);
        return result;
    }


    /**
     * 获取设备信息
     * @param request
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/device/info", method = RequestMethod.GET)
    @ResponseBody
    public Object device(HttpServletRequest request, @RequestParam Long deviceId,
                         @JwtUser Session session){
        if(session.isNotLogin()){
            return Result.fail(AuthErrorType.INVALID_LOGIN);
        }
        Long userId = session.getUserId();


        MapBuilder mapBuilder = newMap(3);

        // 设备信息
        Device device = deviceService.getById(deviceId);
        DeviceOnline deviceOnline = deviceOnlineService.selectByDeviceNo(device.getDeviceNo());
        JSONObject deviceInfo = (JSONObject) JSON.toJSON(device);

        if(deviceOnline != null){
            deviceInfo.put("intranetIp", deviceOnline.getIntranetIp());
            deviceInfo.put("status", deviceOnline.getStatus());
        } else {
            deviceInfo.put("status", -1);
        }


        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userId", userId);
        wrapper.eq("device_id", deviceId);
        wrapper.eq("protocol", 2);

        List<DeviceMapping> portList = deviceMappingService.list(wrapper);


        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("userId", userId);
        wrapper2.eq("device_id", deviceId);
        wrapper2.ne("protocol", 2);

        List<DeviceMapping> domainList = deviceMappingService.list(wrapper2);


        mapBuilder.setParam("deviceInfo", deviceInfo);

        mapBuilder.setParam("features", newMap(4)
                .setParam("domainCount", domainList.size())
                .setParam("portCount", portList.size())
                .setParam("lineName", "成都")
                .setParam("broadband", "10MB")
                .build());

        // 端口
        mapBuilder.setParam("portList", portList);
        // 域名
        mapBuilder.setParam("domainList", domainList);




        return MessageFactory.get(  mapBuilder.build());
    }







}
