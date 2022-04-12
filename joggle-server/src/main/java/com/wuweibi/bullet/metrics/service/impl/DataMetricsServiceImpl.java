package com.wuweibi.bullet.metrics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.metrics.mapper.DataMetricsMapper;
import com.wuweibi.bullet.metrics.entity.DataMetrics;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 数据收集(DataMetrics)表服务实现类
 *
 * @author marker
 * @since 2021-11-07 14:17:49
 */
@Slf4j
@Service
public class DataMetricsServiceImpl extends ServiceImpl<DataMetricsMapper, DataMetrics> implements DataMetricsService {

    @Override
    public boolean generateDayByTime(Date date) {
        // 不支持今日统计
        if (DateUtils.truncatedCompareTo(date, new Date(), Calendar.DATE) == 0) {
            throw new RuntimeException("不支持当日统计");
        }
        return this.baseMapper.generateDayByTime(date);
    }


    /**
     * 每天凌晨0.30执行昨日数据
     *
     * @return
     */
    @Scheduled(cron = "0 1 0 * * *")
    public void generateDayByTimeYesterday() {
        log.info("处理昨日流量数据。。。。");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        this.baseMapper.generateDayByTime(date);
    }
}
