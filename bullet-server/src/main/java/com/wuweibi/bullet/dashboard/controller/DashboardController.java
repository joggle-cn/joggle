package com.wuweibi.bullet.dashboard.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.service.CountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 仪表盘接口
 * @author marker
 **/
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

}
