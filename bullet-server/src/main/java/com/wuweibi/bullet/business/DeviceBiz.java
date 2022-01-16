package com.wuweibi.bullet.business;

public interface DeviceBiz {


    /**
     * 关闭用户所有映射
     * @param userId 用户Id
     */
    void closeAllMappingByUserId(Long userId);
}
