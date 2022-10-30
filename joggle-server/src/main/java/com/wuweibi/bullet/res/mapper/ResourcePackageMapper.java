package com.wuweibi.bullet.res.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.res.domain.ResourcePackageAdminParam;
import com.wuweibi.bullet.res.domain.ResourcePackageListVO;
import com.wuweibi.bullet.res.domain.ResourcePackageParam;
import com.wuweibi.bullet.res.domain.ResourcePackageVO;
import com.wuweibi.bullet.res.entity.ResourcePackage;
import org.apache.ibatis.annotations.Param;

/**
 * (ResourcePackage)表数据库访问层
 *
 * @author marker
 * @since 2022-10-30 15:48:49
 */
public interface ResourcePackageMapper extends BaseMapper<ResourcePackage> {

    Page<ResourcePackageVO> selectAdminList(Page pageInfo, @Param("params") ResourcePackageAdminParam params);

    Page<ResourcePackageListVO> selectWebList(Page pageInfo, @Param("params")  ResourcePackageParam params);
}
