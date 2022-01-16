package com.wuweibi.bullet.dashboard.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.dashboard.domain.DeviceCountInfoVO;
import com.wuweibi.bullet.dashboard.domain.DeviceDateItemVO;
import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.service.CountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 仪表盘接口
 * @author marker
 **/
@Api(tags = "仪表盘")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {


    @Resource
    private CountService countService;

    /**
     * 统计数据接口
     * @return
     */
    @GetMapping("/statistics")
    @ResponseBody
    public R<UserCountVO> statistics(@JwtUser Session session){
        Long userId = session.getUserId();
        UserCountVO countVO = countService.getUserCountInfo(userId);
        return R.success(countVO);
    }

    /**
     * 统计设备流量排行
     * @return
     */
    @ApiOperation("统计设备流量排行")
    @GetMapping("/device/rank")
    @ResponseBody
    public R<List<DeviceCountInfoVO>> getUserDeviceRank(
            @RequestParam("type") Integer type,
            @JwtUser Session session){
        Long userId = session.getUserId();
        List<DeviceCountInfoVO> countVO = countService.getUserDeviceRank(userId, type);
        return R.success(countVO);
    }



    /**
     * 统计设备流量排行
     * @return
     */
    @ApiOperation("统计设备流量走势")
    @GetMapping("/device/trend")
    @ResponseBody
    public R<List<DeviceCountInfoVO>> getUserDeviceTrend(
            @RequestParam(value = "deviceId",required = false) Long deviceId,
            @JwtUser Session session){
        Long userId = session.getUserId();
        List<DeviceDateItemVO> list = countService.getUserDeviceTrend(userId, deviceId);
        return R.success(list);
    }

}
