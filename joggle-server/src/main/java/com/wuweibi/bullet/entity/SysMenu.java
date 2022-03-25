package com.wuweibi.bullet.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author marker
 * @since 2020-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 级别
     */
    @TableField("level")
    private Integer level;

    /**
     * 菜单类型（1运营，2商户）
     */
    @TableField("type")
    private Integer type;

    /**
     * 菜单路径
     */
    @TableField("url")
    private String url;

    /**
     * 菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 菜单名称
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 逻辑删除(1删除，0未删除)
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 更新人ID
     */
    @TableField("updated_user_id")
    private Long updatedUserId;

    /**
     * 创建人ID
     */
    @TableField("created_user_id")
    private Long createdUserId;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;


}
