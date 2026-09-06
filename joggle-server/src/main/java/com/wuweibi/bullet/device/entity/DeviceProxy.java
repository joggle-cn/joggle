package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "id")
  	private Long id;
    
    /**
     * 设备id
     */
    @Schema(description = "设备id")
 	private Long deviceId;
    
    /**
     * 设备代理服务端口
     */
    @Schema(description = "设备代理服务端口")
 	private Integer deviceProxyPort;
    
    /**
     * 端口id
     */
    @Schema(description = "端口id")
 	private Integer domainId;
    
    /**
     * 代理协议：http/https/socks5
     */
    @Schema(description = "代理协议：http/https/socks5")
 	private String type;

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

    @Schema(description = "状态 1正常 0 停用")
    private Integer status;

}
