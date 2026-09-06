package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户设备列表查询参数
 *
 * @author marker
 * @create 2026-08-01
 **/
@Data
public class DeviceWebQueryParam {

    @Schema(description = "设备状态 1在线 0离线")
    private Integer status;

    @Schema(description = "操作系统（模糊匹配）")
    private String os;

    @Schema(description = "排序字段 name=名称 latency=延迟，默认按在线时间")
    private String sort;

    @Schema(description = "排序方向 asc/desc，默认 desc")
    private String order;
}
