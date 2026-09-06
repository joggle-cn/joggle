package com.wuweibi.bullet.res.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "资源包名称")
 	private String name;

    /**
     * 等级
     */        
    @Schema(description = "等级")
 	private Integer level;

    /**
     * 状态 1正常 0禁用
     */        
    @Schema(description = "状态 1正常 0禁用")
 	private Integer status;


}
