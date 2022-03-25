package com.wuweibi.bullet.oauth2.domain;

import com.wuweibi.bullet.common.domain.BasePo;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 接口资源对象
 *
 * @author marker
 */
@Data
@NoArgsConstructor
public class Resource extends BasePo {

    /**
     * 资源代码
     */
    private String code;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源类型
     */
    private String type;
    /**
     * 资源URL
     */
    private String url;

    /**
     * 资源方法
     */
    private String method;

    /**
     * 资源描述
     */
    private String description;
}
