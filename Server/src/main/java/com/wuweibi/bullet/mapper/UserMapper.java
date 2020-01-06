package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wuweibi.bullet.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
public interface UserMapper extends BaseMapper<User> {

    void updatePass(
            @Param("userId")  long userId,
            @Param("pass")  String pass);


    /**
     * 更新登录时间
     * @param userId
     */
    void updateLoginTime(Long userId);

    /**
     * 根据用户名查询1个用户
     * @param username 用户名称
     *
     * @return
     */
    @Select("SELECT id,username,password,enabled,account_non_expired,credentials_non_expired,account_non_locked,name,mobile,created_time,updated_time,created_by,updated_by" +
            " FROM t_sys_users WHERE username = #{username} or mobile = #{username} limit 1")
    User getByUsername(String username);

}