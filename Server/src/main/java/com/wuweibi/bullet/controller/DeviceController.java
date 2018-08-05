package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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


    @Autowired
    private CoonPool coonPool;


    /** 设备管理 */
    @Autowired
    private DeviceService deviceService;


    @Autowired
    private DeviceOnlineService deviceOnlineService;

    @Autowired
    private DeviceMappingService deviceMappingService;


    @RequestMapping(value = "/device/", method = RequestMethod.GET)
    public Object device(HttpServletRequest request ){
        Long userId = getUserId();

        List<Device> list  =deviceService.selectByMap(newMap(1)
                .setParam("userId", userId)
                .build());

        Iterator<Device> it = list.iterator();

        List<DeviceDto> deviceList = new ArrayList<>();

        while (it.hasNext()){
            Device device = it.next();
            DeviceDto deviceDto = new DeviceDto(device);
            String deviceCode = device.getDeviceId();

            int status = getStatus(deviceCode);
            deviceDto.setStatus(status);

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


    // 删除设备
    @RequestMapping(value = "/device/", method = RequestMethod.DELETE)
    public Object save(
                       @RequestParam Long id,
                       HttpServletRequest request ){
        Long userId = getUserId(request);

        // 校验设备是否是他的
        boolean status = deviceService.exists(userId, id);
        if(status){
            // 删除映射
            deviceMappingService.deleteByDeviceId(id);
            deviceService.deleteById(id);


        }
        return MessageFactory.getOperationSuccess();
    }




    // 设备校验
    @RequestMapping(value = "/device/validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(String deviceId,  HttpServletRequest request){
        Long userId = getUserId(request);

        // 没有输入设备ID
        if(StringUtils.isEmpty(deviceId)){
            return MessageFactory.get(State.DeviceNotInput);
        }


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
