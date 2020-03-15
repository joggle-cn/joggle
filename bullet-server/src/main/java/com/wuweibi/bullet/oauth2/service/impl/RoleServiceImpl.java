package com.wuweibi.bullet.oauth2.service.impl;

import com.wuweibi.bullet.oauth2.dao.RoleMapper;
import com.wuweibi.bullet.oauth2.service.RoleService;
import com.wuweibi.bullet.oauth2.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * 角色服务
 *
 * @author marker
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Set<Role> queryUserRolesByUserId(long userId) {
        return roleMapper.queryByUserId(userId);
    }

}
