package com.wuweibi.bullet.res.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.res.entity.UserPackageRights;
import org.apache.ibatis.annotations.Param;

/**
 * 用户套餐权益(UserPackageRights)表数据库访问层
 *
 * @author marker
 * @since 2022-11-01 12:41:56
 */
public interface UserPackageRightsMapper extends BaseMapper<UserPackageRights> {

    boolean checkByResourceTypeAndId(@Param("resourceType") Integer resourceType, @Param("resourceId") Long resourceId);
}
