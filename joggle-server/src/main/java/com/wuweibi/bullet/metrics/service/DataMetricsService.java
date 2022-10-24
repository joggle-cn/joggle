package com.wuweibi.bullet.metrics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.metrics.domain.DataMetricsDTO;
import com.wuweibi.bullet.metrics.entity.DataMetrics;

import java.util.Date;

/**
 * 数据收集(DataMetrics)表服务接口
 *
 * @author marker
 * @since 2021-11-07 14:17:49
 */
public interface DataMetricsService extends IService<DataMetrics> {

    boolean generateDayByTime(Date date);

    R uploadData(DataMetricsDTO dataMetrics);

}