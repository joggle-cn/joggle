package com.wuweibi.bullet.oauth2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.oauth2.domain.OauthUser;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 角色Mapper
 *
 * @author marker
 *
 */
public interface OauthUserMapper extends BaseMapper<OauthUser> {


    /**
     * 根据用户名查询1个用户
     * @param username 用户名称
     *
     * @return
     */
    @Select("SELECT id,nickname,username,password,enabled,account_non_expired,credentials_non_expired,account_non_locked,created_time,updated_time,created_by,updated_by,user_admin" +
            " FROM t_sys_users WHERE email = #{username} or username = #{username} limit 1")
    OauthUser getByUsername(String username);



    /**
     * 更新登录时间
     * @param userId
     */
    @Update("update t_sys_users set loginTime =now() where id =#{userId}")
    void updateLoginTime(Long userId);

    @Select("SELECT id,nickname,username,password,enabled,account_non_expired,credentials_non_expired,account_non_locked,created_time,updated_time,created_by,updated_by" +
            " FROM t_sys_users WHERE email = #{username} limit 1")
    OauthUser getByEmail(String email);
}
