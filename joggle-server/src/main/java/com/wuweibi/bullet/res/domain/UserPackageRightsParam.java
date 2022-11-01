package com.wuweibi.bullet.res.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 用户套餐权益(UserPackageRights)分页对象
 *
 * @author marker
 * @since 2022-11-01 12:41:56
 */
@SuppressWarnings("serial")
@Data
public class UserPackageRightsParam {

    /**
     * id
     */        @ApiModelProperty("id")
  	private Long id;    

    /**
     * 用户id
     */        
    @ApiModelProperty("用户id")
 	private Long userId;

    /**
     * 资源类型 1域名 2端口 
     */        
    @ApiModelProperty("资源类型 1域名 2端口 ")
 	private Integer resourceType;

    /**
     * 资源id
     */        
    @ApiModelProperty("资源id")
 	private Long resourceId;

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

    /**
     * 状态 1正常 0禁用
     */        
    @ApiModelProperty("状态 1正常 0禁用")
 	private Integer status;

}
