package com.wuweibi.bullet.domain2.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户域名(UserDomain)分页对象
 *
 * @author marker
 * @since 2023-09-17 17:44:11
 */
@SuppressWarnings("serial")
@Data
public class UserDomainParam {

    @ApiModelProperty("id")
  	private Long id;    

    /**
     * 用户id
     */        
    @ApiModelProperty("用户id")
 	private Long userId;


}
