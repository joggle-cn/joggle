package com.wuweibi.bullet.system.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户实名认证(UserCertification)分页对象
 *
 * @author marker
 * @since 2022-09-14 13:54:44
 */
@SuppressWarnings("serial")
@Data
public class UserCertificationVO {

    /**
     * id
     */        @Schema(description = "id")
  	private Long id;    

    /**
     * 用户id
     */        
    @Schema(description = "用户id")
 	private Long userId;

    /**
     * 类型1身份证
     */        
    @Schema(description = "类型1身份证")
 	private Integer type;

    /**
     * 真实姓名
     */        
    @Schema(description = "真实姓名")
 	private String realName;

    /**
     * 身份证号码
     */        
    @Schema(description = "身份证号码")
 	private String idcard;

    /**
     * 手机号码
     */        
    @Schema(description = "手机号码")
 	private String phone;

    /**
     * 出生日期
     */        
    @Schema(description = "出生日期")
 	private Date birthday;

    /**
     * 性别
     */        
    @Schema(description = "性别")
 	private String sex;

    /**
     * 省份
     */        
    @Schema(description = "省份")
 	private String province;

    /**
     * 城市
     */        
    @Schema(description = "城市")
 	private String city;

    /**
     * 区县
     */        
    @Schema(description = "区县")
 	private String district;

    /**
     * 地区
     */        
    @Schema(description = "地区")
 	private String area;

    /**
     * 创建时间
     */        
    @Schema(description = "创建时间")
 	private Date createTime;

    /**
     * 认证 结果 1通过 0等待审核  2未通过
     */        
    @Schema(description = "认证 结果 1通过 0等待审核  2未通过")
 	private Integer result;

    /**
     * 不通过原因
     */        
    @Schema(description = "不通过原因")
 	private String resultMsg;

}
