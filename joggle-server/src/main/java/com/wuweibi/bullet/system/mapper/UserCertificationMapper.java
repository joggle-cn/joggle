package com.wuweibi.bullet.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.system.entity.UserCertification;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 用户实名认证(UserCertification)表数据库访问层
 *
 * @author marker
 * @since 2022-09-14 13:54:43
 */
public interface UserCertificationMapper extends BaseMapper<UserCertification> {

    boolean checkIdcardAndPhone(@Param("phone") String phone, @Param("idcard") String idcard);
}
