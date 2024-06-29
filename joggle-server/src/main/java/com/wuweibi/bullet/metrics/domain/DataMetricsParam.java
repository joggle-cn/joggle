package com.wuweibi.bullet.metrics.domain;

import lombok.Data;

/**
 * 数据收集(DataMetrics)表实体类
 *
 * @author marker
 * @since 2021-11-07 14:17:46
 */
@SuppressWarnings("serial")
@Data
public class DataMetricsParam {

    /**
     * 设备编号
     */
    private Long deviceId;
    /**
     * 设备映射ID
     */
    private Long mappingId;

    private Long userId;


    private Integer serverTunnelId;


}
