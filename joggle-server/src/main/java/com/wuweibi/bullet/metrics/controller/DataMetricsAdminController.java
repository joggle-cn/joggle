package com.wuweibi.bullet.metrics.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.metrics.domain.DataMetricsListVO;
import com.wuweibi.bullet.metrics.domain.DataMetricsParam;
import com.wuweibi.bullet.metrics.entity.DataMetrics;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import com.wuweibi.bullet.service.DeviceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据收集(DataMetrics)表控制层
 *
 * @author marker
 * @since 2021-11-07 14:17:51
 */
@RestController
@RequestMapping("/admin/data/metrics")
public class DataMetricsAdminController {
    /**
     * 服务对象
     */
    @Resource
    private DataMetricsService dataMetricsService;
    @Resource
    private DeviceService deviceService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @return 所有数据
     */
    @ApiOperation("流量明细分页查询")
    @GetMapping("/list")
    public R<Page<DataMetricsListVO>> getPage(Page<DataMetrics> page, DataMetricsParam params) {
        return R.ok(this.dataMetricsService.getList(page, params));
    }


}
