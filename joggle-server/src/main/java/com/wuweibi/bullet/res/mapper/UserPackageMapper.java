package com.wuweibi.bullet.res.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.res.domain.UserPackageExpireVO;
import com.wuweibi.bullet.res.entity.UserPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户套餐(UserPackage)表数据库访问层
 *
 * @author marker
 * @since 2022-10-31 09:15:53
 */
public interface UserPackageMapper extends BaseMapper<UserPackage> {





    List<UserPackageExpireVO> selectByExpireDay( );


    boolean updateToLevel0ByUserId(@Param("userId") Long userId);

}
