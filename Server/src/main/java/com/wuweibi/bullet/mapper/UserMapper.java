package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
}