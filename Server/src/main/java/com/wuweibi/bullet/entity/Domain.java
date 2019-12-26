package com.wuweibi.bullet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
@TableName("t_domain")
public class Domain extends Model<Domain> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 二级域名前缀或端口
	 */
	private String domain;

	/**
	 *  类型： 1 端口 2域名
	 */
	private Integer type;

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
	 * 发售时间
	 */
    @TableField(value = "create_time")
	private Date createTime;

	/**
	 * 购买时间
	 */
	@TableField(value = "buy_time")
	private Date buyTime;
	/**
	 * 到期时间
	 */
	@TableField(value = "due_time")
	private Date dueTime;

	/**
	 * 所有人ID
	 */
	@TableField(value = "user_id")
	private Long userId;

	/**
	 * 状态：1已售、0释放、-1 禁售
	 */
	@TableField(value = "status")
	private Integer status;

    @Override
	protected Serializable pkVal() {
		return this.id;
	}

}
