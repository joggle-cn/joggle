
package com.wuweibi.bullet.domain2.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户域名(UserDomain)分页对象
 *
 * @author marker
 * @since 2023-09-17 17:44:10
 */
@SuppressWarnings("serial")
@Data
public class UserDomainAddDTO {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 域名
     */
    @ApiModelProperty("域名")
    @NotBlank(message = "域名不能为空")
    private String domain;



}
