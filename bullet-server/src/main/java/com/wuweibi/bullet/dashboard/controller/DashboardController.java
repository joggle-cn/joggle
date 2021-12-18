package com.wuweibi.bullet.dashboard.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.dashboard.domain.DeviceCountInfoVO;
import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.Result;
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
    public Result<UserCountVO> statistics(@JwtUser Session session){
        Long userId = session.getUserId();
        UserCountVO countVO = countService.getUserCountInfo(userId);
        return Result.success(countVO);
    }

    /**
     * 统计设备流量排行
     * @return
     */
    @ApiOperation("统计设备流量排行")
    @GetMapping("/device/rank")
    @ResponseBody
    public Result<List<DeviceCountInfoVO>> getUserDeviceRank(
            @RequestParam("type") Integer type,
            @JwtUser Session session){
        Long userId = session.getUserId();
        List<DeviceCountInfoVO> countVO = countService.getUserDeviceRank(userId, type);
        return Result.success(countVO);
    }

}
