package com.wuweibi.bullet.controller;


import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static com.wuweibi.bullet.builder.MapBuilder.newMap;
import static com.wuweibi.bullet.utils.SessionHelper.getUserId;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Controller
@RequestMapping("/api/user/device/mapping")
public class DeviceMappingController {


    /** 端口映射服务 */
    @Autowired
    private DeviceMappingService deviceMappingService;


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delete(HttpServletRequest request,@RequestParam Long id){
        Long userId = getUserId(request);

        boolean status = deviceMappingService.exists(userId, id);

        if(status){
            deviceMappingService.deleteById(id);
        }
        return MessageFactory.getOperationSuccess();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Object device(HttpServletRequest request,@RequestParam Long deviceId){
        Long userId = getUserId(request);
        return MessageFactory.get(deviceMappingService.selectByMap(newMap(2)
                .setParam("userId", userId)
                .setParam("device_id", deviceId)
                .build()));
    }



    // 保存数据
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Object save(
            DeviceMapping entity,
                HttpServletRequest request ){
        Long userId = getUserId(request);
        entity.setUserId(userId);

        boolean status = false;
        if(entity.getId() != null){
            status = deviceMappingService.updateById(entity);
        } else {
            // 验证域名是否被使用
            boolean isOk = deviceMappingService.existsDomain(entity.getDomain());
            if(isOk){
                return MessageFactory.get(State.DomainIsUsed);
            }

            status = deviceMappingService.insert(entity);
        }
        if(status){
            return MessageFactory.getOperationSuccess();
        }
        return MessageFactory.getErrorMessage("服务器错误");
    }



}
