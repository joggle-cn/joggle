package com.wuweibi.bullet.oauth2.service;


import com.wuweibi.bullet.oauth2.domain.Resource;

import java.util.Set;


/**
 * 接口资源服务
 *
 * @author marker
 */
public interface ResourceService {


    /**
     * 返回所有的资源定义内容，resources表中
     *
     * @return Set<Resource>
     */
    Set<Resource> findAll();


    /**
     * 根据角色code查询到角色把对应的资源定义
     *
     * @param roleCodes 角色编码数组
     * @return Set<Resource>
     */
    Set<Resource> queryByRoleCodes(String[] roleCodes);
}
