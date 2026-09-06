package com.wuweibi.bullet.device.domain.dto;


import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "设备任意门")
@Data
public class DeviceDoorDTO {

    @Schema(description = "设备id")
    @NotNull(message = "设备id不能空")
    private Long deviceId;

    @Schema(description = "域名id")
    @NotNull(message = "请选择一个域名")
    private Long domainId;

    @Schema(description = "本地路径")
    @NotNull(message = "本地路径不能空")
    private String localPath;

    @Schema(description = "启用状态")
    @Max(value = 1, message = "启用状态不正确")
    @Min(value = 0, message = "启用状态不正确")
    private Integer enable;
}
