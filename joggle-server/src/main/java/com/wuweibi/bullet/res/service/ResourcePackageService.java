package com.wuweibi.bullet.res.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.res.entity.ResourcePackage;
import com.wuweibi.bullet.res.domain.ResourcePackageVO;
import com.wuweibi.bullet.res.domain.ResourcePackageParam;

/**
 * (ResourcePackage)表服务接口
 *
 * @author marker
 * @since 2022-10-30 15:48:47
 */
public interface ResourcePackageService extends IService<ResourcePackage> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<ResourcePackageVO> getPage(Page pageInfo, ResourcePackageParam params);



    Page<ResourcePackageVO> getAdminList(Page pageInfo, ResourcePackageParam params);
}
