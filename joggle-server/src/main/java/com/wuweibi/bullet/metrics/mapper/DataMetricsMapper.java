package com.wuweibi.bullet.metrics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.metrics.domain.DataMetricsListVO;
import com.wuweibi.bullet.metrics.domain.DataMetricsParam;
import com.wuweibi.bullet.metrics.entity.DataMetrics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 数据收集(DataMetrics)表数据库访问层
 *
 * @author marker
 * @since 2021-11-07 14:17:52
 */
public interface DataMetricsMapper extends BaseMapper<DataMetrics> {



    boolean generateDayByTime(@Param("date") Date date);

    Page<DataMetricsListVO> selectUserList(Page<DataMetrics> page, @Param("params")  DataMetricsParam params);
}