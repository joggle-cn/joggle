package com.wuweibi.bullet.device.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *   设备映射
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Data
public class MappingDeviceVO {

	private Long id;
	@TableField("device_id")
	private Long deviceId;

	private String domain;

	private Integer port;


	/**
	 * 域名或端口ID
	 */
	@TableField("domain_id")
	private Long domainId;

	/**
	 * 远端端口
	 */
    @TableField("remote_port")
	private Integer remotePort;
	/**
	 * hostname
	 */
	private String hostname;

	/**
	 * 简单认证
	 * "demo:secret"
	 */
	private String auth;

	/**
	 * 服务器地址
	 * （null 为本机）
	 */
	private String host;


	private Boolean bindTls;


	private Long userId;

	/** 协议 1 HTTP 2 TCP 3HTTPS 4 HTTP/HTTPS */
	private Integer protocol;

	/** 备注 */
	private String description;

    /**
     * 映射状态 （1、启用；0、停用)
     * */
    private Integer status;
    /**
     * 创建时间
     */
	private Date createTime;

	/**
	 * 服务通道ID
	 */
	private Integer serverTunnelId;


	private BigDecimal todayFlow;


}
