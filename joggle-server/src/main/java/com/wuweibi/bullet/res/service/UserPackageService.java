package com.wuweibi.bullet.res.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.res.entity.UserPackage;
import com.wuweibi.bullet.res.domain.UserPackageVO;
import com.wuweibi.bullet.res.domain.UserPackageParam;

/**
 * 用户套餐(UserPackage)表服务接口
 *
 * @author marker
 * @since 2022-10-31 09:15:53
 */
public interface UserPackageService extends IService<UserPackage> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<UserPackageVO> getPage(Page pageInfo, UserPackageParam params);

    UserPackage getByUserId(Long userId);

    boolean updateToLevel0ByUserId(Long userId, UserPackage userPackage);

    boolean checkPackageId(Integer packageId);


    /**
     * 更新套餐流量
     * (带有保护，不支持负数)当更新失败时，走用户流量走
     * @param userId
     * @param flow
     * @return
     */
    boolean updateFLow(Long userId, long flow);
}
