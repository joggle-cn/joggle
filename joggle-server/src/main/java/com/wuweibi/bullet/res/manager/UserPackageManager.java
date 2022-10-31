package com.wuweibi.bullet.res.manager;

import com.wuweibi.bullet.entity.api.R;

public interface UserPackageManager {

    /**
     * 检查设备限制
     * @param userId
     * @return
     */
    boolean checkLimit(Long userId, UserPackageLimitEnum userPackageLimitEnum, int addNum);

    R usePackageAdd(Long userId, UserPackageLimitEnum deviceNum, int num);

    R openService(Long userId, Integer packageId, Integer dueTime);
}
