package com.wuweibi.bullet.domain2.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户域名(UserDomain)分页对象
 *
 * @author marker
 * @since 2023-09-17 17:44:10
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
public class UserDomainOptionVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "自定义域名")
    private String name;

    @Schema(description = "已经绑定 true 未绑定 false")
    private Boolean bind;

}
