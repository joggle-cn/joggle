package com.wuweibi.bullet.device.domain.vo;


import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "设备任意门")
@Data
public class DeviceDoorVO {

    @Schema(description = "设备id")
    private Long deviceId;

    @Schema(description = "域名id")
    private Long domainId;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "本地路径")
    private String localPath;

    @Schema(description = "启用状态")
    private Integer enable;
}
