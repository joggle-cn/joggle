package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.vo.CountVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface CountMapper {


    CountVO selectCountInfo();

    UserCountVO selectUserCountInfo(@Param("userId") Long userId);
}
