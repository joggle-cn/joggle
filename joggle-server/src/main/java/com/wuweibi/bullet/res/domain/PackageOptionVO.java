package com.wuweibi.bullet.res.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 套餐下拉对象
 *
 * @author marker
 * @since 2022-10-30 15:48:50
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
public class PackageOptionVO {

    /**
     * id
     */
    @Schema(description = "id")
  	private Integer id;    

    /**
     * 资源包名称
     */        
    @Schema(description = "资源包名称")
 	private String name;
    /**
     * 资源包名称(标签)
     */
    @Schema(description = "资源包名称(标签)")
 	private String label;

}
