package com.wuweibi.bullet.domain2.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户域名(UserDomain)表实体类
 *
 * @author marker
 * @since 2023-09-17 17:44:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDomain {
    
    @TableId
    @ApiModelProperty("id")
  	private Long id;
    
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
 	private Long userId;
    
    /**
     * 域名
     */
    @ApiModelProperty("域名")
 	private String domain;

    @ApiModelProperty("是否配置证书 1已配置 0未配置")
 	private Boolean isCert;
    @ApiModelProperty("是否自动续期 1自动 0关闭")
 	private Boolean isAutoRenewal;

    /**
     * 证书私钥
     */
    @ApiModelProperty("证书私钥")
 	private String certKey;

    /**
     * 账号私钥
     */
    @ApiModelProperty("账号私钥")
 	private String accountKey;

    /**
     * 证书pem
     */
    @ApiModelProperty("证书pem")
 	private String certPem;


    /**
     * 证书颁发时间
     */
    @ApiModelProperty("证书颁发时间")
 	private Date applyTime;

    @ApiModelProperty("证书颁发失败原因")
    private String applyError;
    
    /**
     * 证书到期时间
     */
    @ApiModelProperty("证书到期时间")
 	private Date dueTime;
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
 	private Date createTime;
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
 	private Date updateTime;

}
