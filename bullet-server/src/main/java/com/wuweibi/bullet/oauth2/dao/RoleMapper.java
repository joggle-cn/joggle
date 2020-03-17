package com.wuweibi.bullet.oauth2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.oauth2.domain.Role;
import org.apache.ibatis.annotations.Select;

import java.util.Set;


/**
 * 角色Mapper
 *
 * @author marker
 *
 */
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return Set<Role>
     */
    @Select("SELECT DISTINCT r.id, r.code,r.name,r.description"
            +  " FROM  t_sys_users_roles_relation urr"
            + " INNER JOIN t_sys_roles r ON r.id = urr.role_id"
            + " WHERE urr.user_id = #{userId}")
    Set<Role> queryByUserId(Long userId);
}
