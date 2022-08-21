package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备代理(DeviceProxy)表实体类
 *
 * @author marker
 * @since 2022-08-19 21:00:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceProxy {
    
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
  	private Long id;
    
    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
 	private Long deviceId;
    
    /**
     * 设备代理服务端口
     */
    @ApiModelProperty("设备代理服务端口")
 	private Integer deviceProxyPort;
    
    /**
     * 端口id
     */
    @ApiModelProperty("端口id")
 	private Integer domainId;
    
    /**
     * 代理协议：http/https/socks5
     */
    @ApiModelProperty("代理协议：http/https/socks5")
 	private String type;

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

    @ApiModelProperty("状态 1正常 0 停用")
    private Integer status;

}
