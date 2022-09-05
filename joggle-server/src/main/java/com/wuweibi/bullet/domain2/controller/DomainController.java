package com.wuweibi.bullet.domain2.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.vo.DomainVO;
import com.wuweibi.bullet.domain2.domain.DomainBuyListVO;
import com.wuweibi.bullet.domain2.domain.DomainSearchParam;
import com.wuweibi.bullet.domain2.domain.vo.DomainDetailVO;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.domain2.enums.DomainTypeEnum;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 我的域名接口
 *
 * @author marker
 * @create 2019-12-26 下午9:19
 **/
@Slf4j
@WebApi
@RestController
@RequestMapping("/api/user/domain")
public class DomainController {




    /**
     * 域名管理
     */
    @Resource
    private DomainService domainService;

    @Resource
    private DeviceMappingService deviceMappingService;


    /**
     * 我的域名列表
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object device(@JwtUser Session session) {
        if (session.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }
        Long userId = session.getUserId();
        List<DomainVO> list = domainService.getListByUserId(userId);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.forEach((item) -> {
            Date dueTime = item.getDueDateTime();
            if (dueTime == null) {
                item.setDueTime("永久有效");
            } else {
                item.setDueTime(simpleDateFormat.format(dueTime));
            }

        });
        return MessageFactory.get(list);
    }

    @Resource
    private ServerTunnelService serverTunnelService;

    /**
     * 获取我的域名信息
     */
    @ApiModelProperty("获取域名信息")
    @GetMapping(value = "/info")
    public R<DomainDetailVO> getInfo(@JwtUser Session session, @RequestParam Long domainId) {
        Long userId = session.getUserId();

        Domain domain = domainService.getById(domainId);
        if (domain == null) {
            return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }
        // 能够查询被别人购买 或自己购买的域名
        if (domain.getUserId() != null && !domain.getUserId().equals(userId)) {
            return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }
        ServerTunnel serverTunnel = serverTunnelService.getById(domain.getServerTunnelId());
        if(serverTunnel == null){
            return R.fail("域名/端口信息有误");
        }
        DomainDetailVO domainDetailVO = new DomainDetailVO();
        BeanUtils.copyProperties(domain, domainDetailVO);
        domainDetailVO.setDomainFull(domain.getDomain());
        if (domain.getType().equals(DomainTypeEnum.DOMAIN.getType())) {
            domainDetailVO.setDomainFull(String.format("%s.%s", domain.getDomain(), serverTunnel.getServerAddr()));
        }
        return R.success(domainDetailVO);
    }

    /**
     * 获取未绑定的域名列表
     * @return
     */
    @ApiOperation("获取未绑定的域名/端口列表")
    @GetMapping(value = "/nobind")
    public R getInfo(
            @ApiParam("类型： 1端口 2域名")
            @RequestParam Integer type) {
        Long userId = SecurityUtils.getUserId();
        List<JSONObject> list = domainService.getListNotBindByUserId(userId, type);
        list.forEach(item -> {
            if (item.getInteger("type") == 2) {
                item.put("domain", item.getString("domain") + "." + item.getString("serverAddr"));
            }
        });
        return R.success(list);
    }

    @Resource
    private DeviceService deviceService;


    /**
     * 域名绑定设备
     * @return
     */
    @ApiOperation("域名绑定设备")
    @PostMapping(value = "/bind" )
    public R bind(@RequestParam Long domainId, @RequestParam Long deviceId) {
        Long userId = SecurityUtils.getUserId();

        // 检查是否该用户的域名
        if (!domainService.checkDomain(userId, domainId)) {
            return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }

        // 检查设备是否该用户的
        if (!deviceService.exists(userId, deviceId)) {
            return R.fail(SystemErrorType.DEVICE_NOT_EXIST);
        }
        // 检查域名是否已经绑定
        DeviceMapping deviceMapping = deviceMappingService.getByDomainId(deviceId, domainId);
        if (deviceMapping != null) {
            return R.fail(SystemErrorType.DEVICE_OTHER_BIND, deviceMapping.getId());
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
        mapping.setCreateTime(new Date());
        if (domainInfo.getType() == 1) {
            mapping.setRemotePort(Integer.parseInt(domainInfo.getDomain()));
            mapping.setProtocol(DeviceMapping.PROTOCOL_TCP);
            mapping.setHost("127.0.0.1");
            mapping.setPort(3389);
        } else {
            mapping.setDomain(domainInfo.getDomain());
            mapping.setProtocol(DeviceMapping.PROTOCOL_HTTP_HTTPS);
            mapping.setHost("127.0.0.1");
            mapping.setPort(80);
        }
        deviceMappingService.save(mapping);
        return R.success(mapping.getId());
    }


    /**
     * 搜索可购买的域名
     */
    @ApiModelProperty("搜索可购买的域名")
    @GetMapping(value = "/search")
    public R<Page<DomainBuyListVO>> searchDomain(Page pageParams, DomainSearchParam params) {
        Page<DomainBuyListVO> page = domainService.getBuyList(pageParams, params);
        return R.success(page);
    }


}
