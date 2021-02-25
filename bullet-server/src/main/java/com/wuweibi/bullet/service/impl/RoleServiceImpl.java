package com.wuweibi.bullet.service.impl;
/**
 * Created by marker on 2018/3/6.
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.entity.Role;
import com.wuweibi.bullet.mapper.RoleMapper;
import com.wuweibi.bullet.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 角色服务
 *
 * @author marker
 * @create 2018-03-06 下午3:50
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Override
    public Page<Role> selectCustomPage(Page<Role> pageInfo, Map<String, Object> params) {
        List<Role> list = this.baseMapper.selectCustomPage(pageInfo, params);
        pageInfo.setRecords(list);
        return pageInfo;
    }
//
//    @Override
//    public void updatePermission(Integer roleId, String permissions) {
//
//        // 更新角色权限
//        List<PermissionSimple> permissionSimpleList = JSON.parseArray(permissions, PermissionSimple.class);
//
//        // 配置角色ID
//        Iterator<PermissionSimple> it = permissionSimpleList.iterator();
//        while (it.hasNext()){
//            it.next().setRoleId(roleId);
//        }
//
//        // 删除角色权限
//        this.baseMapper.deletePermisssion(roleId);
//
//
//        this.baseMapper.updatePermission(permissionSimpleList);
//    }
//
//    @Override
//    public List<PermissionSimple> selectRolePermissions(Integer roleId) {
//        return this.baseMapper.selectRolePermissions(roleId, 1);
//    }



}
