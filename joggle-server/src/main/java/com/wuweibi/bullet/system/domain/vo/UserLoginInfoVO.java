package com.wuweibi.bullet.system.domain.vo;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserLoginInfoVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "头像")
    private String icon;

    @Schema(description = "登录时间")
    private String loginTime;

    @Schema(description = "注册时间")
    private Date createdTime;

    @Schema(description = "余额")
    private String balance;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "激活码")
    private String activateCode;

    @Schema(description = "用户认证状态")
    private Integer userCertification;

    @Schema(description = "系统通知 1打开 0关闭")
    private Integer systemNotice;

    @Schema(description = "套餐ID")
    private Integer resourcePackageId;

    @Schema(description = "套餐名称")
    private String resourcePackageName;

    @Schema(description = "套餐等级")
    private Integer resourcePackageLevel;

    @Schema(description = "套餐剩余流量MB")
    private Long userPackageFlow;

    @Schema(description = "套餐总流量MB")
    private Long userPackageFlowTotal;

    @Schema(description = "套餐结束时间")
    private Date packageEndTime;

    @Schema(description = "连接数")
    private Integer connNums;

    @Schema(description = "用户流量MB")
    private Long userFlow;

    @Schema(description = "用户总流量MB")
    private Long userFlowTotal;

    @Schema(description = "认证结果消息")
    private String ucResultMsg;

    @Schema(description = "认证审核时间")
    private Date ucExamineTime;
}
