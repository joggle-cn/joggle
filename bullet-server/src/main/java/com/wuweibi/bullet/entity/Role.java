package com.wuweibi.bullet.entity;
/**
 * Created by marker on 2018/3/15.
 */

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色
 *
 * @author marker
 * @create 2018-03-15 上午10:39
 **/
@Data
@TableName("t_sys_roles")
public class Role implements Serializable {

//`id`, `type`, `client_type`, `code`, `name`, `description`, `deleted`,
// `created_by`, `updated_by`, `updated_user_id`,
//`created_user_id`, `created_time`, `updated_time`, `supplier_id`
    /** ID */
    @TableId
    @TableField("id")
    private Integer id;

    /**
     * 角色名称
     */
    @TableField("code")
    private String code;
    /**
     * 角色名称
     */
    @TableField("name")
    private String name;
    /**
     * 备注
     */
    @TableField(value = "description",fill = FieldFill.INSERT_UPDATE)
    private String description;

    /**
     * 是否删除 1是 0否
     */
    @TableField("deleted")
    private Integer deleted;




}
