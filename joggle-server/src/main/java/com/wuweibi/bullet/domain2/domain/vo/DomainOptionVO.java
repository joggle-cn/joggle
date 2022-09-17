
package com.wuweibi.bullet.domain2.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *  	域名与端口
 * </p>
 *
 * @author marker
 */
@Data
public class DomainOptionVO  {

	/**
	 * 全名称域名
	 */
	private String domainFull;

	private Long id;

	/**
	 * 通道id
	 */
	private Integer serverTunnelId;


	/**
	 *  类型： 1 端口 2 域名
	 */
	private Integer type;


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



}
