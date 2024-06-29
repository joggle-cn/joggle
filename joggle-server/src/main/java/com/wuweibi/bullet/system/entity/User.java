package com.wuweibi.bullet.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	private String email;
	private String nickname;
	private String phone;
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

	/**
	 * 系统通知 1打开 0 关闭
	 */
	private Integer systemNotice;

	/**
	 * 短信通知 1打开 0关闭
	 */
	private Integer smsNotice;



}
