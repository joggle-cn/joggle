package com.wuweibi.bullet.metrics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.metrics.mapper.DataMetricsMapper;
import com.wuweibi.bullet.metrics.entity.DataMetrics;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import org.springframework.stereotype.Service;

/**
 * 数据收集(DataMetrics)表服务实现类
 *
 * @author marker
 * @since 2021-11-07 14:17:49
 */
@Service
public class DataMetricsServiceImpl extends ServiceImpl<DataMetricsMapper, DataMetrics> implements DataMetricsService {

}
