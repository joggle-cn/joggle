package com.wuweibi.bullet.device.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备代理(DeviceProxy)分页对象
 *
 * @author marker
 * @since 2022-08-19 21:00:28
 */
@SuppressWarnings("serial")
@Data
public class DeviceProxyParam {

    /**
     * id
     */        @Schema(description = "id")
  	private Integer id;    

    /**
     * 设备id
     */        
    @Schema(description = "设备id")
 	private Integer deviceId;

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

}
