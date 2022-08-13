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

    @ApiModelProperty("p2p应用名称")
 	private String appName;
    
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


    @ApiModelProperty("服务侧本地Host 默认: 0.0.0.0")
    private String serverLocalHost;


    @ApiModelProperty("客户侧代理Host 默认: 127.0.0.1")
    private String clientProxyHost;


    @ApiModelProperty("备注")
    private String remark;

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
