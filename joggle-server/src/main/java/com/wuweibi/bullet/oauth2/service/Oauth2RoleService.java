package com.wuweibi.bullet.oauth2.service;

import com.wuweibi.bullet.oauth2.domain.Role;

import java.util.Set;


/**
 *
 * 角色服务
 *
 * @author marker
 */
public interface Oauth2RoleService {


    /**
     * 根据用户ID查询角色
     * @param userId 用户ID
     * @return
     */
    Set<Role> queryUserRolesByUserId(long userId);

}

