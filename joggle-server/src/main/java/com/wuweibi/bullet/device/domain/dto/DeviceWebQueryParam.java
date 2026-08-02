package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户设备列表查询参数
 *
 * @author marker
 * @create 2026-08-01
 **/
@Data
public class DeviceWebQueryParam {

    @ApiModelProperty("设备状态 1在线 0离线")
    private Integer status;

    @ApiModelProperty("操作系统（模糊匹配）")
    private String os;

    @ApiModelProperty("排序字段 name=名称 latency=延迟，默认按在线时间")
    private String sort;

    @ApiModelProperty("排序方向 asc/desc，默认 desc")
    private String order;
}
