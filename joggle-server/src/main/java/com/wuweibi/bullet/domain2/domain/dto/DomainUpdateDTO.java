package com.wuweibi.bullet.domain2.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class DomainUpdateDTO {

    private static final long serialVersionUID = 1L;

	private Long id;


	/**
	 * 销售价格（元/月）
	 */
	@TableField(value = "sales_price")
	private BigDecimal salesPrice;
	/**
	 * 销售价格（元/月）
	 */
	@TableField(value = "original_price")
	private BigDecimal originalPrice;


	/**
	 * 到期时间
	 */
	@TableField(value = "due_time")
	private Date dueTime;


	/**
	 * 状态：1已售、0释放、-1 禁售
	 */
	@TableField(value = "status")
	private Integer status;


	@ApiModelProperty("宽带mbps")
	@TableField(value = "bandwidth")
	private Integer bandwidth;

	@ApiModelProperty("并发连接数 每秒")
	private Integer concurrentNum;



}
