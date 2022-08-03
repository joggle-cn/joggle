package com.wuweibi.bullet.device.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("设备任意门")
@Data
public class DeviceDoorDTO {

    @ApiModelProperty("设备id")
    @NotNull(message = "设备id不能空")
    private Long deviceId;

    @ApiModelProperty("域名id")
    @NotNull(message = "请选择一个域名")
    private Long domainId;

    @ApiModelProperty("本地路径")
    @NotNull(message = "本地路径不能空")
    private String localPath;

    @ApiModelProperty("启用状态")
    @Max(value = 1, message = "启用状态不正确")
    @Min(value = 0, message = "启用状态不正确")
    private Integer enable;
}
