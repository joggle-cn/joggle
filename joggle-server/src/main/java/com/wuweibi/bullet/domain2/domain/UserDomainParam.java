package com.wuweibi.bullet.domain2.domain;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户域名(UserDomain)分页对象
 *
 * @author marker
 * @since 2023-09-17 17:44:11
 */
@SuppressWarnings("serial")
@Data
public class UserDomainParam {

    @Schema(description = "id")
  	private Long id;    

    /**
     * 用户id
     */        
    @Schema(description = "用户id")
 	private Long userId;


}
