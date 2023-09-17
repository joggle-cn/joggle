package com.wuweibi.bullet.domain2.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户域名(UserDomain)分页对象
 *
 * @author marker
 * @since 2023-09-17 17:44:10
 */
@SuppressWarnings("serial")
@Data
public class UserDomainVO {

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

    /**
     * 证书私钥
     */
    @ApiModelProperty("证书私钥")
    private String certKey;

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
