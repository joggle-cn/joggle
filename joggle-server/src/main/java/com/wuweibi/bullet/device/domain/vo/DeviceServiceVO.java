
package com.wuweibi.bullet.device.domain.vo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DeviceServiceVO {

    @Schema(description = "服务id")
    private Long id;
    @Schema(description = "设备id")
    private Long deviceId;
    @Schema(description = "服务名称")
    private String name;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "uri地址")
    private String uri;
    @Schema(description = "类型 决定图标样式 打开方式")
    private String type;
}
