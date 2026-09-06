package com.wuweibi.bullet.system.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "id")
    private Integer id;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;

    /**
     * 配置key
     */
    @Schema(description = "配置key")
    private String key;

    /**
     * 配置值
     */
    @Schema(description = "配置值")
    private String value;



}
