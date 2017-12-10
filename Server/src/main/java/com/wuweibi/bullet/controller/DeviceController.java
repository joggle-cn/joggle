package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.wuweibi.bullet.builder.MapBuilder.newMap;
import static com.wuweibi.bullet.utils.SessionHelper.getUserId;

/**
 *
 *
 * @author marker
 * @create 2017-12-06 下午9:19
 **/
@RestController
@RequestMapping("/api/user")
public class DeviceController {


    /** 设备管理 */
    @Autowired
    private DeviceService deviceService;


    @Autowired
    private DeviceOnlineService deviceOnlineService;


    @RequestMapping(value = "/device/", method = RequestMethod.GET)
    @ResponseBody
    public Object device(HttpServletRequest request ){
        Long userId = getUserId();
        return MessageFactory.get(deviceService.selectByMap(newMap(1)
                .setParam("userId", userId)
                .build()));
    }


    // 更新设备
    @RequestMapping(value = "/device/", method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestParam String name,
                       @RequestParam String id,
                       HttpServletRequest request ){
        Long userId = getUserId(request);

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, id);
        if(status){
            deviceService.updateName(id, name);
        }



        return MessageFactory.getOperationSuccess();
    }



    // 设备校验
    @RequestMapping(value = "/device/validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(@RequestParam String deviceId,  HttpServletRequest request){
        Long userId = getUserId(request);

        EntityWrapper ew=new EntityWrapper();
        ew.setEntity(new DeviceOnline());
        ew.where("deviceId = {0}", deviceId).andNew("status = {0}", 1).orderBy("updateTime", false);

        // 验证是否存在
        DeviceOnline deviceOnline = deviceOnlineService.selectOne(ew);
        if(deviceOnline != null){
            // 验证是否绑定
            boolean isBinded = deviceService.existsDevice(deviceId);
            if(isBinded){
                return MessageFactory.get(State.DeviceIdBinded);
            }


            // 给当前用户存储最新的设备数据
            Device device = new Device();
            device.setDeviceId(deviceId);
            device.setUserId(userId);
            device.setCreateTime(new Date());
            device.setName("default");

            deviceService.insert(device);


            return MessageFactory.getOperationSuccess();
        }
        return MessageFactory.get(State.DeviceNotOnline);
    }


    @RequestMapping(value = "/uuid", method = RequestMethod.GET)
    @ResponseBody
    public Map uuid(HttpServletRequest request ){

        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        JSONObject result = new JSONObject();
        result.put("uuid", uuid);
        return result;
    }







}
