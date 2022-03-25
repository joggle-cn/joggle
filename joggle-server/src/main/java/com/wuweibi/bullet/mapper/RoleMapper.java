package com.wuweibi.bullet.mapper;
/**
 * Created by marker on 2018/3/6.
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.entity.Role;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * RoleMapper
 *
 * @author marker
 * @create 2018-03-06 下午3:45
 **/
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 根据公司ID查询角色
     * @param companyId 公司ID
     * @return
     */
//    List<Role> selectByCompanyId(Serializable companyId);


    /**
     * 自定义分页查询
     * @param pageInfo 分页信息
     * @param params 参数
     * @return
     */
    List<Role> selectCustomPage(Page<Role> pageInfo, Map<String, Object> params);


    /**
     * 更新权限
     * @param permissionSimpleList 权限数据
     */
//    void updatePermission(List<PermissionSimple> permissionSimpleList);


    /**
     * 删除角色
     * @param roleId 角色ID
     */
//    void deletePermisssion(Serializable roleId);


    /**
     * 角色的所有权限
     * @param roleId 角色Id
     * @param userType 用户类型
     * @return
     */
//    List<PermissionSimple> selectRolePermissions(@Param("roleId") Integer roleId, @Param("userType") Integer userType);


    /**
     * 检查角色ID是否存在用户
     * @param roleId 角色Id
     * @return
     */
    boolean checkUser(Serializable roleId);


    /**
     * 获取总公司权限
     * @return
     */
//    List<PermissionSimple> getHeadCompanyPermissionsAll();


    /**
     * 删除角色集合的权限数据
     * @param idList 角色集合
     */
    void deleteRolesPermisssion(List<Integer> idList);


}
