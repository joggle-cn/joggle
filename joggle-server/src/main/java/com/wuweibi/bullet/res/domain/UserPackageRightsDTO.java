package com.wuweibi.bullet.res.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "用户id")
 	private Long userId;

    /**
     * 资源类型 1域名 2端口 
     */        
    @Schema(description = "资源类型 1域名 2端口 ")
 	private Integer resourceType;

    /**
     * 资源id
     */        
    @Schema(description = "资源id")
 	private Long resourceId;

    /**
     * 状态 1正常 0禁用
     */        
    @Schema(description = "状态 1正常 0禁用")
 	private Integer status;

}
