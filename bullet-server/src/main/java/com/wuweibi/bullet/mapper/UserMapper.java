package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

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


    /**
     * 更新用户余额
     * @param userId 用户ID
     * @param payMoney 操作金额
     * @return
     */
    @Update("update t_sys_users set balance = balance + #{payMoney} where id = #{userId} and ((balance + #{payMoney}) >= 0)")
    boolean updateBalance(@Param("userId") Long userId, @Param("payMoney")  BigDecimal payMoney);


    /**
     * 给用户赋权角色
     * (不会验证权限是否存在的)
     * @param userId
     * @param roleCode
     */
    @Update("insert into t_sys_users_roles_relation(user_id, role_id) values(#{userId}, (select id from t_sys_roles where code = #{roleCode} limit 1))")
    void saveNewAuthRole(@Param("userId") Long userId,@Param("roleCode")  String roleCode);


}