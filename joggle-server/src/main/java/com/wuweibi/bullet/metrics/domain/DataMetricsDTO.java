package com.wuweibi.bullet.metrics.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据收集(DataMetrics)表实体类
 *
 * @author marker
 * @since 2021-11-07 14:17:46
 */
@SuppressWarnings("serial")
@Data
public class DataMetricsDTO  {

    /**
     * 设备编号
     */
    @NotBlank(message = "deviceNo不能为空")
    private String deviceNo;
    /**
     * 设备映射ID
     */
    @NotNull(message = "设备映射ID不能为空")
    private Long mappingId;
    /**
     * 进入流量
     */
    @NotNull(message = "进入流量不能为空")
    private Long bytesIn;

    /**
     * 出口流量
     */
    @NotNull(message = "出口流量不能为空")
    private Long bytesOut;

    private Long openTime;

    private Long closeTime;

    private String remoteAddr;

}
