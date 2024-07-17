package com.wuweibi.bullet.metrics.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据收集(DataMetrics)表实体类
 *
 * @author marker
 * @since 2021-11-07 14:17:46
 */
@SuppressWarnings("serial")
@Data
public class DataMetricsHourSettleDTO {

    /**
     * 设备编号
     */
    @NotNull(message = "结算时间 yyyyMMddHH")
    @DateTimeFormat(pattern = "yyyyMMddHH")
    @JsonFormat(pattern = "yyyyMMddHH")
    private Date date;


}
