package com.wuweibi.bullet.domain2.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户域名(UserDomain)分页对象
 *
 * @author marker
 * @since 2023-09-17 17:44:10
 */
@SuppressWarnings("serial")
@Data
public class UserDomainDTO {

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
     * 证书私钥
     */        
    @Schema(description = "证书私钥")
 	private String certKey;

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
