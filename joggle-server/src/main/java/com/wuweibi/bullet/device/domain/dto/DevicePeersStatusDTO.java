package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * (DevicePeers)分页对象
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@SuppressWarnings("serial")
@Data
public class DevicePeersStatusDTO {

    /**
     * id
     */
    @Schema(description = "id")
  	private Long id;    

    /**
     * 状态 1启用 0禁用
     */        
    @Schema(description = "状态 1启用 0禁用")
 	private Integer status;



}
