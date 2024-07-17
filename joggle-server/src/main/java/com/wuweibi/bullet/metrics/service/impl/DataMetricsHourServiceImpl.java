package com.wuweibi.bullet.metrics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.metrics.domain.DataMetricsHourParam;
import com.wuweibi.bullet.metrics.domain.DataMetricsHourVO;
import com.wuweibi.bullet.metrics.entity.DataMetricsHour;
import com.wuweibi.bullet.metrics.mapper.DataMetricsHourMapper;
import com.wuweibi.bullet.metrics.service.DataMetricsHourService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 数据收集(小时)(DataMetricsHour)表服务实现类
 *
 * @author marker
 * @since 2024-07-17 18:02:16
 */
@Service
public class DataMetricsHourServiceImpl extends ServiceImpl<DataMetricsHourMapper, DataMetricsHour> implements DataMetricsHourService {


    @Override
    public Page<DataMetricsHourVO> getPage(Page pageInfo, DataMetricsHourParam params) {

        LambdaQueryWrapper<DataMetricsHour> qw = Wrappers.lambdaQuery();
        Page<DataMetricsHour> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<DataMetricsHourVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            DataMetricsHourVO vo = new DataMetricsHourVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }
}
