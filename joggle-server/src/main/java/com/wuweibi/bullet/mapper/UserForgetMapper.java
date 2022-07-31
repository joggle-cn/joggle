package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.UserForget;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
public interface UserForgetMapper extends BaseMapper<UserForget> {


    /**
     * 更新Email状态
     */
    void updateEmailStatus(String email);

    UserForget findByCode(String code);


    void updateStatus(String code);


    /**
     * 检查最近是否申请过忘记密码
     *
     * @param email 邮箱地址
     * @param time  最近时间 单位秒
     * @return
     */
    boolean checkApply(@Param("email") String email, @Param("time") int time);
}