package com.wuweibi.bullet.flow.controller;


import com.wuweibi.bullet.flow.service.UserFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户流量(UserFlow)表控制层
 *
 * @author marker
 * @since 2022-01-09 15:47:13
 */
@Slf4j
@RestController
@Tag(name = "用户流量")
@RequestMapping("/admin/hall/userFlow")
public class UserFlowController  {
    /**
     * 服务对象
     */
    @Resource
    private UserFlowService userFlowService;


}
