package com.wuweibi.bullet.device.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("设备任意门")
@Data
public class DeviceDoorVO {

    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("域名id")
    private Long domainId;

    @ApiModelProperty("域名")
    private String domain;

    @ApiModelProperty("本地路径")
    private String localPath;

    @ApiModelProperty("启用状态")
    private Integer enable;
}
