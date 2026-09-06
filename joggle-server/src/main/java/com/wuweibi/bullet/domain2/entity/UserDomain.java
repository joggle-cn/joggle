package com.wuweibi.bullet.domain2.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "id")
  	private Long id;
    
    /**
     * 用户id
     */
    @Schema(description = "用户id")
 	private Long userId;
    
    /**
     * 域名
     */
    @Schema(description = "域名")
 	private String domain;

    @Schema(description = "是否配置证书 1已配置 0未配置")
 	private Boolean isCert;
    @Schema(description = "是否自动续期 1自动 0关闭")
 	private Boolean isAutoRenewal;

    /**
     * 证书私钥
     */
    @Schema(description = "证书私钥")
 	private String certKey;

    /**
     * 账号私钥
     */
    @Schema(description = "账号私钥")
 	private String accountKey;

    /**
     * 证书pem
     */
    @Schema(description = "证书pem")
 	private String certPem;


    /**
     * 证书颁发时间
     */
    @Schema(description = "证书颁发时间")
 	private Date applyTime;

    @Schema(description = "证书颁发失败原因")
    private String applyError;
    
    /**
     * 证书到期时间
     */
    @Schema(description = "证书到期时间")
 	private Date dueTime;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
 	private Date createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
 	private Date updateTime;

}
