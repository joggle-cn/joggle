package com.wuweibi.bullet.metrics.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 数据收集(DataMetrics)表实体类
 *
 * @author marker
 * @since 2021-11-07 14:17:46
 */
@SuppressWarnings("serial")
@Data
public class DataMetricsListVO {
    /**
     * id
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 设备ID
     */
    private Long deviceId;

    private String deviceName;

    private String deviceNo;
    /**
     * 设备映射ID
     */
    private Long mappingId;

    private Integer serverTunnelId;

    private String serverTunnelName;
    /**
     * 进入流量
     */
    private Long bytesIn;
    /**
     * 出口流量
     */
    private Long bytesOut;

    private Long flow;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 创建天
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    /**
     * 创建月
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createMonth;

    /**
     * 创建年
     */
    private Integer createYear;

    @ApiModelProperty("链接数量")
    private Long link;


    public String getFlowStr() {
        if (this.flow < 1024) {
            return String.format("%dB", this.flow);
        }
        if (this.flow < 1024 * 1024) {
            return String.format("%dKB", this.flow / 1024);
        }
        if (this.flow < 1024 * 1024 * 1024) {
            return String.format("%dMB", this.flow / 1024 / 1024);
        }
        if (this.flow < 1024 * 1024 * 1024 * 1024) {
            return String.format("%dGB", this.flow / 1024 / 1024 / 1024);
        }
        return String.format("%dGB", this.flow / 1024 / 1024 / 1024);
    }
}
