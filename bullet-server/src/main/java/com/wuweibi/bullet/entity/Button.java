package com.wuweibi.bullet.entity;
/**
 * Created by marker on 2018/4/11.
 */

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * 按钮
 *
 * @author marker
 * @create 2018-04-11 13:24
 **/
@Data
@TableName("DRM_MODULE_BUTTON")
public class Button implements Serializable {

    public Button(){}

    /**
     * 模块ID
     */
    @TableField("MODULEID")
    private Integer moduleId;

    /**
     * key
     */
    @TableField("KEY")
    private String key;
    /**
     * 名称
     */
    @TableField("NAME")
    private String name;
    /**
     * 描述
     */
    @TableField("DESC")
    private String desc;


}
