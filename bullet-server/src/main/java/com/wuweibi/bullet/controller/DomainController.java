package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public Object device( @JwtUser Session session){
        if(session.isNotLogin()){
            return Result.fail(AuthErrorType.INVALID_LOGIN);
        }
        Long userId = session.getUserId();
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


    /**
     * 获取我的域名信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object getInfo(@JwtUser Session session, @RequestParam Long domainId){

        Long userId = session.getUserId();

        // 检查是否该用户的域名
        if(!domainService.checkDomain(userId, domainId)){
            return Result.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }
        Domain domain = domainService.getById(domainId);

        return Result.success(domain);
    }


    @Resource
    private OrderPayBiz orderPayBiz;


    /**
     * 计算价格
     * @param session
     * @return
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public Object calculate(@JwtUser Session session,
                          @RequestParam Integer time, // 3w
                          @RequestParam Long domainId){

        Long userId = session.getUserId();

        // 检查是否该用户的域名
        if(!domainService.checkDomain(userId, domainId)){
            return Result.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }


        return orderPayBiz.calculate(domainId, time);
    }



    /**
     * 支付接口
     * @param session
     * @return
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Object pay(@JwtUser Session session,
                      @RequestParam(defaultValue = "1") Integer payType, // 3w
                      @RequestParam Integer time, // 3w
                      @RequestParam Long domainId){

        Long userId = session.getUserId();

        // 检查是否该用户的域名
        if(!domainService.checkDomain(userId, domainId)){
            return Result.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }


        Result result = orderPayBiz.calculate(domainId, time);

        if(result.isSuccess()){
            BigDecimal payMoney = result.getDataMapAsBigDecimal("payMoney");
            Long  dueTime   = result.getDataMapAsLong("dueTime");
            switch (payType){
                case 1:
                    orderPayBiz.balancePay(userId, domainId, payMoney, dueTime);
                    break;
            }

        }


        return result;
    }


    /**
     * 获取未绑定的域名列表
     * @param session
     * @return
     */
    @RequestMapping(value = "/nobind", method = RequestMethod.GET)
    public Object getInfo(@JwtUser Session session){
        Long userId = session.getUserId();
        List<JSONObject> list = domainService.getListNotBindByUserId(userId);

        list.forEach(item->{
            if( item.getInteger("type") == 2){
                item.put("domain", item.getString("domain")+".joggle.cn");
            }
        });

        return Result.success(list);
    }

    @Resource
    private DeviceService deviceService;


    /**
     * 域名绑定设备
     * @param session
     * @return
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public Object getInfo(@JwtUser Session session,
                          @RequestParam Long domainId,
                          @RequestParam Long deviceId){
        Long userId = session.getUserId();



        // 检查是否该用户的域名
        if(!domainService.checkDomain(userId, domainId)){
            return Result.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }

        // 检查设备是否该用户的
        if(!deviceService.exists(userId, deviceId)){
            return Result.fail(SystemErrorType.DEVICE_NOT_EXIST);
        }
        // 检查域名是否已经绑定
        if(deviceMappingService.existsDomainId(deviceId, domainId)){
            return Result.fail(SystemErrorType.DEVICE_OTHER_BIND);
        }

        // 执行绑定
        Domain domainInfo = domainService.getById(domainId);
        Device deviceInfo = deviceService.getById(deviceId);

        DeviceMapping mapping = new DeviceMapping();
        mapping.setDomain(domainInfo.getDomain());
        mapping.setUserId(userId);
        mapping.setDeviceId(deviceId);
        mapping.setServerTunnelId(deviceInfo.getServerTunnelId());
        mapping.setDomainId(domainId);
        if(domainInfo.getType() == 1){
            mapping.setRemotePort(Integer.parseInt(domainInfo.getDomain()));
            mapping.setProtocol(DeviceMapping.PROTOCOL_TCP);
        } else {
            mapping.setDomain(domainInfo.getDomain());
            mapping.setProtocol(DeviceMapping.PROTOCOL_HTTP);
        }
        deviceMappingService.save(mapping);
        return Result.success();
    }





}
