package com.wuweibi.bullet.domain2.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  	域名与端口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Data
public class DomainAddDTO {

	@ApiModelProperty("服务提通道ID")
	@NotNull(message = "服务提通道ID不能空")
	private Integer serverTunnelId;

	/**
	 *  类型： 1 端口 2 域名
	 */
	@ApiModelProperty("类型： 1 端口 2 域名")
	@NotNull(message = "资源类型不能为空")
	private Integer type;

	/**
	 * 二级域名前缀或端口
	 */
	@NotBlank(message = "域名不能为空")
	private String domain;

	/**
	 * 销售价格（元/月）
	 */
	@ApiModelProperty("销售价格（元/月）")
	private BigDecimal salesPrice;
	/**
	 * 销售价格（元/月）
	 */
	@TableField(value = "original_price")
	private BigDecimal originalPrice;


	/**
	 * 到期时间
	 */
	@ApiModelProperty("到期时间 yyyy-MM-dd HH:mm:ss")
	private Date dueTime;


	/**
	 * 状态：1已售、0释放、-1 禁售
	 */
	@ApiModelProperty("状态：1已售、0释放、-1 禁售")
	private Integer status;


	@ApiModelProperty("宽带mbps")
	@TableField(value = "bandwidth")
	@Min(value = 1,message = "宽带必须大于等于1") @Max(1000)
	private Integer bandwidth;

	@Min(value = 1,message = "并发连接数必须大于等于1") @Max(10000)
	@ApiModelProperty("并发连接数 每秒")
	private Integer concurrentNum;



}
