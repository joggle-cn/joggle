package com.wuweibi.bullet.domain2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.domain2.domain.UserDomainParam;
import com.wuweibi.bullet.domain2.domain.UserDomainVO;
import com.wuweibi.bullet.domain2.entity.UserDomain;
import org.apache.ibatis.annotations.Param;

/**
 * 用户域名(UserDomain)表数据库访问层
 *
 * @author marker
 * @since 2023-09-17 17:44:10
 */
public interface UserDomainMapper extends BaseMapper<UserDomain> {


    /**
     * 分页查询用户域名
     * @param pageInfo 分页对象
     * @param params 条件参数
     * @return
     */
    Page<UserDomainVO> selectListPage(Page pageInfo, @Param("params") UserDomainParam params);
}
