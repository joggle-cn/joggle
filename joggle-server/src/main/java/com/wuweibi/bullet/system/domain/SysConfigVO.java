package com.wuweibi.bullet.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统配置表(SysConfig)分页对象
 *
 * @author marker
 * @since 2024-06-23 20:35:30
 */
@SuppressWarnings("serial")
@Data
public class SysConfigVO {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 配置key
     */
    @ApiModelProperty("配置key")
    private String key;

    /**
     * 配置值
     */
    @ApiModelProperty("配置值")
    private String value;



}
