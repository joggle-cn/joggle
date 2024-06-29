package com.wuweibi.bullet.res.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户套餐权益(UserPackageRights)分页对象
 *
 * @author marker
 * @since 2022-11-01 12:41:56
 */
@SuppressWarnings("serial")
@Data
public class UserPackageRightsDTO {

    /**
     * 用户id
     */        
    @ApiModelProperty("用户id")
 	private Long userId;

    /**
     * 资源类型 1域名 2端口 
     */        
    @ApiModelProperty("资源类型 1域名 2端口 ")
 	private Integer resourceType;

    /**
     * 资源id
     */        
    @ApiModelProperty("资源id")
 	private Long resourceId;

    /**
     * 状态 1正常 0禁用
     */        
    @ApiModelProperty("状态 1正常 0禁用")
 	private Integer status;

}
