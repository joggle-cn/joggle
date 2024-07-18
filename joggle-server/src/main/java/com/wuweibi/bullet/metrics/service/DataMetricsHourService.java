package com.wuweibi.bullet.metrics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.metrics.entity.DataMetricsHour;
import com.wuweibi.bullet.metrics.domain.DataMetricsHourVO;
import com.wuweibi.bullet.metrics.domain.DataMetricsHourParam;

/**
 * 数据收集(小时)(DataMetricsHour)表服务接口
 *
 * @author marker
 * @since 2024-07-17 18:02:16
 */
public interface DataMetricsHourService extends IService<DataMetricsHour> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<DataMetricsHourVO> getPage(Page pageInfo, DataMetricsHourParam params);
}
