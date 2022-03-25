package com.wuweibi.bullet.oauth2.service.impl;

import com.wuweibi.bullet.oauth2.dao.Oauth2RoleMapper;
import com.wuweibi.bullet.oauth2.domain.Role;
import com.wuweibi.bullet.oauth2.service.Oauth2RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * 角色服务
 *
 * @author marker
 */
@Service
public class Oauth2RoleServiceImpl implements Oauth2RoleService {

    @Autowired
    private Oauth2RoleMapper roleMapper;

    @Override
    public Set<Role> queryUserRolesByUserId(long userId) {
        return roleMapper.queryByUserId(userId);
    }

}
