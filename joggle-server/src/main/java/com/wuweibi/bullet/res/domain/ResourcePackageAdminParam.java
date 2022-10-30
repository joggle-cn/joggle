package com.wuweibi.bullet.res.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ResourcePackage)分页对象
 *
 * @author marker
 * @since 2022-10-30 15:48:50
 */
@SuppressWarnings("serial")
@Data
public class ResourcePackageAdminParam {

    /**
     * 资源包名称
     */        
    @ApiModelProperty("资源包名称")
 	private String name;

    /**
     * 等级
     */        
    @ApiModelProperty("等级")
 	private Integer level;

    /**
     * 状态 1正常 0禁用
     */        
    @ApiModelProperty("状态 1正常 0禁用")
 	private Integer status;


}
