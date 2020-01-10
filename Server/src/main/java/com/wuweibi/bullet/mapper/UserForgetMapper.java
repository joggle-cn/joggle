package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.entity.UserForget;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
public interface UserForgetMapper extends BaseMapper<UserForget> {



    /**
     * 更新Email状态
     * */
    void updateEmailStatus(String email);

    UserForget findByCode(String code);


    void updateStatus(String code);
}