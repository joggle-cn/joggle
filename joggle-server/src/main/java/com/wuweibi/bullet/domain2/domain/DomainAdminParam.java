package com.wuweibi.bullet.domain2.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
	@Schema(description = "通道id")
	private Integer serverTunnelId;

	/**
	 * 二级域名前缀或端口
	 */
	@Schema(description = "二级域名前缀或端口")
	private String domain;


	@Schema(description = "类型： 1 端口 2 域名")
	private Integer type;

	@Schema(description = "用户id")
	private Long userId;


	/**
	 * 状态：1已售、0释放、-1 禁售
	 */
	@Schema(description = "状态：1已售、0释放、-1 禁售")
	private Integer status;




}
