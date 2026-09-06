package com.wuweibi.bullet.system.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 *    用户模型
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
@Data
@TableName("t_sys_users")
public class UserDetailVO extends Model<UserDetailVO> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	private String email;
	private String nickname;
	private String username;
	private String password;
	private String agree;

	/**
	 * 头像
	 */
	@TableField("icon")
	private String icon;

	/**
	 * 登录时间
	 */
	@TableField("loginTime")
	private Date loginTime;

	@TableField("created_time")
	private Date createdTime;

	/**
	 * 余额
	 */
	@TableField("balance")
	private BigDecimal balance;

	/**
	 * 是否启用
	 */
	@TableField("enabled")
	private boolean enabled;


	/**
	 * 激活码/邀请码
	 */
	@TableField("activate_code")
	private String activateCode;


	private Integer userCertification;

	@Schema(description = "系统通知 1打开 0关闭")
	private Integer systemNotice;

	@Schema(description = "套餐id")
	private Integer resourcePackageId;

	@Schema(description = "套餐名称")
	private String resourcePackageName;

	@Schema(description = "套餐等级")
	private Integer resourcePackageLevel;

	@Schema(description = "套餐剩余流量KB")
	private Long userPackageFlow;

	@Schema(description = "套餐总流量KB")
	private Long userPackageFlowTotal;


	@Schema(description = "套餐结束时间")
	private Date packageEndTime;




}
