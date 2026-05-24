package com.wuweibi.bullet.system.controller;


import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.res.domain.UserPackageInfoVO;
import com.wuweibi.bullet.res.entity.ResourcePackage;
import com.wuweibi.bullet.res.entity.UserPackage;
import com.wuweibi.bullet.res.service.ResourcePackageService;
import com.wuweibi.bullet.res.service.UserPackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 用户套餐(UserPackage)表控制层
 *
 * @author marker
 * @since 2022-10-31 09:15:53
 */
@Slf4j
@RestController
@WebApi
@Api(value = "用户套餐", tags = "用户套餐")
@RequestMapping("/api/user/package")
public class UserPackageApiController {
    /**
     * 服务对象
     */
    @Resource
    private UserPackageService userPackageService;

    @Resource
    private ResourcePackageService resourcePackageService;
    
    /**
     * 登录用户的套餐信息
     * @return 所有数据
     */
    @ApiOperation("登录用户的套餐信息")
    @GetMapping("/info")
    public R<UserPackageInfoVO> getUserPackInfo() {
        if (SecurityUtils.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }
        Long userId = SecurityUtils.getUserId();

        UserPackage userPackage = userPackageService.getByUserId(userId);
        UserPackageInfoVO vo = new UserPackageInfoVO();

        if (userPackage == null) {
            vo.setStatus(-1);
            vo.setStatusText("未开通");
            vo.setName("普通用户");
            vo.setLevel(0);
            return R.success(vo);
        }

        ResourcePackage resourcePackage = null;
        if (userPackage.getResourcePackageId() != null) {
            resourcePackage = resourcePackageService.getById(userPackage.getResourcePackageId());
        }

        vo.setName(userPackage.getName());
        vo.setLevel(userPackage.getLevel());
        vo.setStartTime(userPackage.getStartTime());
        vo.setEndTime(userPackage.getEndTime());
        vo.setUpdateTime(new Date());

        Date now = new Date();
        if (userPackage.getEndTime() != null && userPackage.getEndTime().after(now)) {
            vo.setStatus(1);
            vo.setStatusText("生效中");
            long diff = userPackage.getEndTime().getTime() - now.getTime();
            vo.setRemainingDays(diff / (1000 * 60 * 60 * 24));
        } else if (userPackage.getEndTime() != null) {
            vo.setStatus(0);
            vo.setStatusText("已过期");
            vo.setRemainingDays(0L);
        } else {
            vo.setStatus(1);
            vo.setStatusText("生效中");
        }

        if (resourcePackage != null && resourcePackage.getDays() != null) {
            int days = resourcePackage.getDays();
            if (days > 0 && days % 365 == 0) {
                vo.setPeriodDesc((days / 365) + "年");
            } else if (days > 0 && days % 30 == 0) {
                vo.setPeriodDesc((days / 30) + "个月");
            } else if (days > 0) {
                vo.setPeriodDesc(days + "天");
            }
        }

        vo.setAutoRenew(0);

        if (resourcePackage != null) {
            vo.setDeviceNum(resourcePackage.getDeviceNum());
            vo.setP2pNum(resourcePackage.getP2pNum());
            vo.setPortNum(resourcePackage.getPortNum());
            vo.setDomainNum(resourcePackage.getDomainNum());
            vo.setConcurrentNum(resourcePackage.getConcurrentNum());
            vo.setBroadbandRate(resourcePackage.getBroadbandRate());
            vo.setWolEnable(resourcePackage.getWolEnable());
            vo.setProxyEnable(resourcePackage.getProxyEnable());
        }

        vo.setDeviceUse(userPackage.getDeviceUse());
        vo.setPeerUse(userPackage.getPeerUse());
        vo.setPortUse(userPackage.getPortUse());
        vo.setDomainUse(userPackage.getDomainUse());
        vo.setConcurrentUse(0);

        vo.setFlowUse(userPackage.getFlowUse());
        vo.setFlowTotal(userPackage.getFlowTotal());
        vo.setFlow(userPackage.getFlow());

        if (userPackage.getFlowTotal() != null && userPackage.getFlowTotal() > 0) {
            long used = userPackage.getFlowUse() != null ? userPackage.getFlowUse() : 0;
            int percent = (int) (used * 100 / userPackage.getFlowTotal());
            vo.setFlowPercent(Math.min(percent, 100));
        } else {
            vo.setFlowPercent(0);
        }

        vo.setFlowResetRule("套餐流量每月1日00:00自动重置，请合理使用。");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        vo.setNextFlowResetTime(calendar.getTime());

        return R.success(vo);
    }


}
