package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        Long userId = getUserId(request);
        return MessageFactory.get(deviceService.selectByMap(newMap(1)
                .setParam("userId", userId)
                .build()));
    }


    @RequestMapping(value = "/device/", method = RequestMethod.POST)
    @ResponseBody
    public Object save(HttpServletRequest request ){
        long userId = getUserId(request);

        return MessageFactory.getOperationSuccess();
    }



    @RequestMapping(value = "/device/validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(@RequestParam String deviceId,  HttpServletRequest request){
        Long userId = getUserId(request);

        EntityWrapper ew=new EntityWrapper();
        ew.setEntity(new DeviceOnline());
        ew.where("deviceId = {0}", deviceId).orderBy("updateTime", false);

        DeviceOnline deviceOnline = deviceOnlineService.selectOne(ew);
        if(deviceOnline != null){

            // 更新在线状态为绑定。

            // 给当前用户存储最新的设备数据





            return MessageFactory.getOperationSuccess();
        }
        return MessageFactory.getErrorMessage("验证失败！");
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
