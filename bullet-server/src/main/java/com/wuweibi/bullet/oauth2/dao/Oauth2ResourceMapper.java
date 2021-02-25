package com.wuweibi.bullet.oauth2.dao;

import com.wuweibi.bullet.oauth2.domain.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;


/**
 * 资源Mapper
 * @author marker
 */
@Mapper
@Repository
public interface Oauth2ResourceMapper {


    /**
     * 查询所有资源
     *
     * @return
     */
    @Select("SELECT id,code,type,name,url,method,description,created_time,updated_time,created_by,updated_by" +
            " FROM t_sys_resources")
    Set<Resource> findAll();


    /**
     * 查询角色编码集合的所有资源
     * @param roleCodes 角色编码集合
     * @return
     */
    @Select("<script>" +
            "SELECT DISTINCT rs.code,rs.url,rs.name,rs.type,rs.method,rs.description" +
            " FROM t_sys_roles r" +
            " INNER JOIN t_sys_roles_resources_relation rrr ON r.id = rrr.role_id" +
            " INNER JOIN t_sys_resources rs ON rs.id = rrr.resource_id" +
            " WHERE r.code IN " +
            " <foreach collection='roleCodes' item='roleCode' index='index' open='(' close=')' separator=',' >" +
            "    #{roleCode}" +
            " </foreach>" +
            "</script>")
    Set<Resource> queryByRoleCodes(@Param("roleCodes") String[] roleCodes);
}
