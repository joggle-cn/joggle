package com.wuweibi.bullet.domain2.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

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

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("自定义域名")
    private String name;

    @ApiModelProperty("已经绑定 true 未绑定 false")
    private Boolean bind;

}
