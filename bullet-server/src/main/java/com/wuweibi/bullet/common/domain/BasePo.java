package com.wuweibi.bullet.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * 基础类（通用字段)
 *
 * @author marker
 */
@Data
public class BasePo implements Serializable {

    /**
     * 默认操作用户名称
     */
    public static final String DEFAULT_USERNAME = "system";

    /**
     * 默认操作用户ID
     */
    public static final Long DEFAULT_USERID = 0L;

    /**
     * ID （自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建用户
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建用户ID
     */
    @TableField("created_user_id")
    private Long createdUserId;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime = new Date();


    /**
     * 是否包含ID
     * @return boolean
     */
    public boolean hasId() {
        return this.id != null;
    }
}
