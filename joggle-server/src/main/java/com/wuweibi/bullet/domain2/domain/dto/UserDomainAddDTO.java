
package com.wuweibi.bullet.domain2.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 域名
     */
    @Schema(description = "域名")
    @NotBlank(message = "域名不能为空")
    private String domain;



}
