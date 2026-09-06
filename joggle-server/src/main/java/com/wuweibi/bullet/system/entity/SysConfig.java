package com.wuweibi.bullet.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 系统配置表(SysConfig)表实体类
 *
 * @author marker
 * @since 2024-06-23 20:35:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysConfig {

    /**
     * id
     */
    @TableId
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
    @TableField("`key`")
    private String key;

    /**
     * 配置值
     */
    @Schema(description = "配置值")
    @TableField("`value`")
    private String value;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;

}
