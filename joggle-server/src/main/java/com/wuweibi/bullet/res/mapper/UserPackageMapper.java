package com.wuweibi.bullet.res.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.res.domain.UserPackageExpireVO;
import com.wuweibi.bullet.res.domain.UserPackageFowVO;
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


    /**
     * 查询到期的用户套餐列表
     * （具体调用是使用的游标）
     * @return
     */
    List<UserPackageExpireVO> selectByExpireDay();

    /**
     * 查询到期的套餐用户
     * @return
     */
    List<UserPackageFowVO> selectByExpiration();


    /**
     * 查询需要充值流量的用户信息
     * @return
     */
    List<UserPackageFowVO> selectByRestPackageFlow( );


    boolean updateToLevel0ByUserId(@Param("userId") Long userId);

    boolean updateFlow(@Param("userId") Long userId, @Param("flow") Long flow);

    boolean updateRestFLow(@Param("userId") Long userId, @Param("flow") Long flow);
}
