package com.wuweibi.bullet.metrics.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.metrics.entity.DataMetrics;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 数据收集(DataMetrics)表控制层
 *
 * @author marker
 * @since 2021-11-07 14:17:51
 */
//@RestController
//@RequestMapping("/api/data/metrics")
public class DataMetricsController  {
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
     * @param dataMetrics 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R<Page<DataMetrics>> selectAll(Page<DataMetrics> page, DataMetrics dataMetrics) {
        return R.success(this.dataMetricsService.page(page, new QueryWrapper<>(dataMetrics)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.success(this.dataMetricsService.getById(id));
    }


    /**
     * 修改数据
     *
     * @param dataMetrics 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody DataMetrics dataMetrics) {
        return R.success(this.dataMetricsService.updateById(dataMetrics));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return R.success(this.dataMetricsService.removeByIds(idList));
    }
}
