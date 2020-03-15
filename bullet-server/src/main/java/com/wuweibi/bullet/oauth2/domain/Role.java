package com.wuweibi.bullet.oauth2.domain;

import com.wuweibi.bullet.common.domain.BasePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 角色
 *
 * @author marker
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BasePo {

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;
}
