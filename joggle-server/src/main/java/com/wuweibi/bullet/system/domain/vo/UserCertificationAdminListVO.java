package com.wuweibi.bullet.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户实名认证(UserCertification)分页对象
 *
 * @author marker
 * @since 2022-09-14 13:54:44
 */
@SuppressWarnings("serial")
@Data
public class UserCertificationAdminListVO {

    /**
     * id
     */
    @ApiModelProperty("id")
  	private Long id;    

    /**
     * 用户id
     */        
    @ApiModelProperty("用户id")
 	private Long userId;

    /**
     * 类型1身份证
     */        
    @ApiModelProperty("类型1身份证")
 	private Integer type;

    /**
     * 真实姓名
     */        
    @ApiModelProperty("真实姓名")
 	private String realName;

    /**
     * 身份证号码
     */        
    @ApiModelProperty("身份证号码")
 	private String idcard;

    /**
     * 手机号码
     */        
    @ApiModelProperty("手机号码")
 	private String phone;

    /**
     * 出生日期
     */        
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
 	private Date birthday;

    /**
     * 性别
     */        
    @ApiModelProperty("性别")
 	private String sex;

    /**
     * 省份
     */        
    @ApiModelProperty("省份")
 	private String province;

    /**
     * 城市
     */        
    @ApiModelProperty("城市")
 	private String city;

    /**
     * 区县
     */        
    @ApiModelProperty("区县")
 	private String district;

    /**
     * 地区
     */        
    @ApiModelProperty("地区")
 	private String area;

    /**
     * 创建时间
     */        
    @ApiModelProperty("创建时间")
 	private Date createTime;

    @ApiModelProperty("审核时间")
 	private Date examineTime;

    /**
     * 认证 结果 1通过 0等待审核  2未通过
     */        
    @ApiModelProperty("认证 结果 1通过 0等待审核  2未通过")
 	private Integer result;

    /**
     * 不通过原因
     */        
    @ApiModelProperty("不通过原因")
 	private String resultMsg;

}
