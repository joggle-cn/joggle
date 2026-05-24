package com.wuweibi.bullet.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserLoginInfoVO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String icon;

    @ApiModelProperty("登录时间")
    private String loginTime;

    @ApiModelProperty("注册时间")
    private Date createdTime;

    @ApiModelProperty("余额")
    private String balance;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("激活码")
    private String activateCode;

    @ApiModelProperty("用户认证状态")
    private Integer userCertification;

    @ApiModelProperty("系统通知 1打开 0关闭")
    private Integer systemNotice;

    @ApiModelProperty("套餐ID")
    private Integer resourcePackageId;

    @ApiModelProperty("套餐名称")
    private String resourcePackageName;

    @ApiModelProperty("套餐等级")
    private Integer resourcePackageLevel;

    @ApiModelProperty("套餐剩余流量MB")
    private Long userPackageFlow;

    @ApiModelProperty("套餐总流量MB")
    private Long userPackageFlowTotal;

    @ApiModelProperty("套餐结束时间")
    private Date packageEndTime;

    @ApiModelProperty("连接数")
    private Integer connNums;

    @ApiModelProperty("用户流量MB")
    private Long userFlow;

    @ApiModelProperty("用户总流量MB")
    private Long userFlowTotal;

    @ApiModelProperty("认证结果消息")
    private String ucResultMsg;

    @ApiModelProperty("认证审核时间")
    private Date ucExamineTime;
}
