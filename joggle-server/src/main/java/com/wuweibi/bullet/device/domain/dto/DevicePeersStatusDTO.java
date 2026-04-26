package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty("id")
  	private Long id;    

    /**
     * 状态 1启用 0禁用
     */        
    @ApiModelProperty("状态 1启用 0禁用")
 	private Integer status;



}
