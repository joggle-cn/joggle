package com.wuweibi.bullet.service;
/**
 * Created by marker on 2018/3/6.
 */


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.entity.Role;

import java.util.Map;

/**
 *
 * 角色Service
 *
 * @author marker
 * @create 2018-03-06 下午3:49
 **/
public interface RoleService extends IService<Role> {

//
//    /**
//     * 获取公司的角色信息
//     * @param companyId 公司ID
//     * @return
//     */
//    List<Role> selectByCompanyId(Integer companyId);
//
//
    /**
     * 自定义分页查询
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<Role> selectCustomPage(Page<Role> pageInfo, Map<String, Object> params);
//
//
//    /**
//     * 批量更新权限
//     * @param id
//     * @param permissionSimpleList
//     */
//    void updatePermission(Integer id, String permissionSimpleList);
//
//    /**
//     * 获取角色的权限
//     * @param roleId
//     * @return
//     */
//    List<PermissionSimple> selectRolePermissions(Integer roleId);
//
//
//    /**
//     * 获取角色权限集合（针对按钮）
//     * @param roleId 角色ID
//     * @param userType 用户类型
//     * @return
//     */
//    List<String> getRolePermissions(Integer userType, Integer roleId);
//
//
//    /**
//     * 批量删除
//     * @param idList 角色ID集合
//     */
//    void batchDelete(List<Integer> idList) throws ServiceException, ServiceException;
//
//
//    /**
//     * 获取总公司菜单所有权限
//     * @return
//     */
//    List<String> getHeadCompanyPermissionsAll();
//
//
//    /**
//     * 保存實體
//     * @param role 角色
//     */
//    void saveEntity(Role role);
//
//
//    /**
//     * 更新實體
//     * @param role 角色
//     */
//    void updateEntity(Role role);
}
