package com.wuweibi.bullet.res.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.res.domain.UserPackageRightsDTO;
import com.wuweibi.bullet.res.entity.UserPackageRights;
import com.wuweibi.bullet.res.domain.UserPackageRightsVO;
import com.wuweibi.bullet.res.domain.UserPackageRightsParam;

/**
 * 用户套餐权益(UserPackageRights)表服务接口
 *
 * @author marker
 * @since 2022-11-01 12:41:56
 */
public interface UserPackageRightsService extends IService<UserPackageRights> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<UserPackageRightsVO> getPage(Page pageInfo, UserPackageRightsParam params);

    void addPackageRights(UserPackageRightsDTO dto);

    boolean checkResource(Integer resourceType, Long domainId);

    boolean removeResourceByUserId(Long userId);

}
