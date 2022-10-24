package com.wuweibi.bullet.system.client.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author marker
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ClientVersionAdminListVO implements Serializable {


    /**
     * id
     */
    private Integer id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 标题
     */
    private String title;


    private String os; // ":"darwin",
    
    private String arch; // ":"amd64",

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;

    /**
     * 下载URL地址
     */
    private String downloadUrl;

    /**
     * 检查信息
     */
    private String checksum;

    /**
     * 状态 1上架 0下架
     */
    private Integer status;


}
