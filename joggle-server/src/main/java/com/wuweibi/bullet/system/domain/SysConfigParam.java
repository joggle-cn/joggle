package com.wuweibi.bullet.system.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 系统配置表(SysConfig)分页对象
 *
 * @author marker
 * @since 2024-06-23 20:35:30
 */
@SuppressWarnings("serial")
@Data
public class SysConfigParam {

/**
     * id
     */    @ApiModelProperty("id")
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

/**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
 	private Date createTime;

/**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
 	private Date updateTime;

}
