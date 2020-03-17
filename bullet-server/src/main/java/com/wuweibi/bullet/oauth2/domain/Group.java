package com.wuweibi.bullet.oauth2.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuweibi.bullet.common.domain.BasePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 * 用户分组（部门)
 *
 * @author marker
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_group")
public class Group extends BasePo {


    /**
     * 分组名
     */
    private String name;

    /**
     * 父级ID
     */
    private long parentId;

    /**
     * 描述
     */
    private String description;
}
