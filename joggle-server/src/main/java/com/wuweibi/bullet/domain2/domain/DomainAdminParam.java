package com.wuweibi.bullet.domain2.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  	域名与端口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Data
public class DomainAdminParam {

	/**
	 * 通道id
	 */
	@ApiModelProperty("通道id")
	private Integer serverTunnelId;

	/**
	 * 二级域名前缀或端口
	 */
	@ApiModelProperty("二级域名前缀或端口")
	private String domain;


	@ApiModelProperty("类型： 1 端口 2 域名")
	private Integer type;

	@ApiModelProperty("用户id")
	private Long userId;


	/**
	 * 状态：1已售、0释放、-1 禁售
	 */
	@ApiModelProperty("状态：1已售、0释放、-1 禁售")
	private Integer status;




}
