package com.wuweibi.bullet.flow.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.flow.domain.UserFlowDTO;
import com.wuweibi.bullet.flow.domain.UserFlowParam;
import com.wuweibi.bullet.flow.domain.UserFlowVO;
import com.wuweibi.bullet.flow.entity.UserFlow;
import com.wuweibi.bullet.flow.service.UserFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 用户流量(UserFlow)表控制层
 *
 * @author marker
 * @since 2022-01-09 15:47:13
 */
@Slf4j
@RestController
@Api(value = "用户流量", tags = "用户流量")
@RequestMapping("/admin/hall/userFlow")
public class UserFlowController  {
    /**
     * 服务对象
     */
    @Resource
    private UserFlowService userFlowService;


}
