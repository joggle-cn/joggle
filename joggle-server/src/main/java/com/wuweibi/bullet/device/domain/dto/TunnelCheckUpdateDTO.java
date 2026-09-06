package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class TunnelCheckUpdateDTO {


    @Schema(description = "通道id")
    @NotNull(message = "{com.wuweibi.bullet.device.id.NotNull}")
    private Integer tunnelId;



}
