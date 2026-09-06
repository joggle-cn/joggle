package com.wuweibi.bullet.domain2.domain;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户域名(UserDomain)分页对象
 *
 * @author marker
 * @since 2023-09-17 17:44:10
 */
@SuppressWarnings("serial")
@Data
public class UserDomainVO {

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

    /**
     * 证书pem
     */
    @Schema(description = "是否配置证书")
    private Boolean isCert;


    @Schema(description = "是否自动续期 1自动 0关闭")
    private Boolean isAutoRenewal;

    /**
     * 证书颁发时间
     */
    @Schema(description = "证书颁发时间")
    private Date applyTime;

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
