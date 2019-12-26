package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.builder.MapBuilder;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.wuweibi.bullet.builder.MapBuilder.newMap;
import static com.wuweibi.bullet.utils.SessionHelper.getUserId;

/**
 *
 * 我的域名接口
 *
 * @author marker
 * @create 2019-12-26 下午9:19
 **/
@Slf4j
@RestController
@RequestMapping("/api/user/domain")
public class DomainController {


    @Autowired
    private CoonPool coonPool;


    /** 域名管理 */
    @Autowired
    private DomainService domainService;


    @Autowired
    private DeviceOnlineService deviceOnlineService;

    @Autowired
    private DeviceMappingService deviceMappingService;


    /**
     * 我的域名列表
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object device(){
        Long userId = getUserId();
        List<JSONObject> list = domainService.getListByUserId(userId);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.forEach((item)->{
            Date dueTime = item.getDate("dueTime");
            if(dueTime == null){
                item.put("dueTime", "永久有效");
            }else{
                item.put("dueTime", simpleDateFormat.format(dueTime));
            }

        });
        return MessageFactory.get(list);
    }






}
