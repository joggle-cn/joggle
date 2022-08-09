package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * (DevicePeers)表实体类
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DevicePeers {
    
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
  	private Long id;
    
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
 	private Long userId;
    
    /**
     * 服务侧设备id
     */
    @ApiModelProperty("服务侧设备id")
 	private Long serverDeviceId;
    
    /**
     * 客户侧设备id
     */
    @ApiModelProperty("客户侧设备id")
 	private Long clientDeviceId;
    
    /**
     * 服务侧本地端口
     */
    @ApiModelProperty("服务侧本地端口")
 	private Integer serverLocalPort;
    
    /**
     * 客户侧代理端口
     */
    @ApiModelProperty("客户侧代理端口")
 	private Integer clientProxyPort;
    
    /**
     * 状态 1启用 0禁用
     */
    @ApiModelProperty("状态 1启用 0禁用")
 	private Integer status;
    
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
