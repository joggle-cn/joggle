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


    /**
     * 到期释放
     * @return
     */
    R expireFree();

    /**
     * 重置流量数量
     */
    void resetPackageFlow();

    /**
     * VIP用户资源包到期前2天提醒，每日一次
     */
    void taskUserPackageExpirationReminder();


}
