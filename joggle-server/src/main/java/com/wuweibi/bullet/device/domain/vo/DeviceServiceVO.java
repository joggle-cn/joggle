
package com.wuweibi.bullet.device.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceServiceVO {

    @ApiModelProperty("服务id")
    private Long id;
    @ApiModelProperty("设备id")
    private Long deviceId;
    @ApiModelProperty("服务名称")
    private String name;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("uri地址")
    private String uri;
    @ApiModelProperty("类型 决定图标样式 打开方式")
    private String type;
}
