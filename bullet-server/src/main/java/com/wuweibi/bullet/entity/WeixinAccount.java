package com.wuweibi.bullet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
@Data
@TableName("t_weixin")
public class WeixinAccount extends Model<WeixinAccount> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;

    @TableField("code")
    private String code;

    @TableField("open_id")
	private String openId;

    @TableField("session_key")
	private String sessionKey;

    @TableField("union_id")
	private String unionId;

    @TableField("time")
	private Date time;

	@TableField("user_id")
	private Long userId;


    private String country;
    private String province;
    private String city;
    private int gender;// 性别
    @TableField("avatar_url")
    private String avatarUrl;
    @TableField("nick_name")
    private String nickName;
    private String language;

    @TableField("jwt_token")
    private String jwtToken;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
