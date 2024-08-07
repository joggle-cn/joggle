package com.wuweibi.bullet.metrics.controller;


import com.wuweibi.bullet.config.properties.JoggleProperties;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.metrics.domain.DataMetricsDTO;
import com.wuweibi.bullet.metrics.domain.DataMetricsHourSettleDTO;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 数据收集(DataMetrics)表控制层
 *
 * @author marker
 * @since 2021-11-07 14:17:51
 */
@Slf4j
@WebApi
@Api(tags = "数据收集")
@RestController
@RequestMapping("/inner/open/data/metrics")
public class DataMetricsOpenInnerController {
    /**
     * 服务对象
     */
    @Resource
    private DataMetricsService dataMetricsService;

    @Resource
    private JoggleProperties joggleProperties;


    /**
     * 上报流量数据
     * （已采用websocket方式上报）
     * @param dataMetrics 实体对象
     * @return 新增结果
     */
    @PostMapping("/up")
    public R insert(@RequestHeader String authorization,
                    @RequestBody @Valid DataMetricsDTO dataMetrics) {
        if (!joggleProperties.getAdminApiToken().equals(authorization)) {
            log.warn("[上报流量]无接口调用权限token:{}", authorization);
            return R.fail("无接口调用权限");
        }
        if (dataMetrics.getCloseTime() <= dataMetrics.getOpenTime()){
            log.warn("[上报流量]链接时长错误");
            return R.fail("链接时长错误");
        }

        return dataMetricsService.uploadData(dataMetrics);
    }


    /**
     * 流量数据小时结算
     * @param authorization
     * @param dataMetrics
     * @return
     */
    @ApiOperation("[内]流量数据小时结算")
    @PostMapping("/hour/settle")
    public R dataMetricsHourSettle(@RequestHeader String authorization,
                    @RequestBody @Valid DataMetricsHourSettleDTO dataMetrics) {
        if (!joggleProperties.getAdminApiToken().equals(authorization)) {
            log.warn("[流量数据小时结算]无接口调用权限token:{}", authorization);
            return R.fail("无接口调用权限");
        }
        dataMetricsService.dataMetricsHourSettle(dataMetrics);
        return R.ok();
    }



}
