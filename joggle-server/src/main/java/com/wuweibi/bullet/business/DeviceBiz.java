package com.wuweibi.bullet.business;

import com.wuweibi.bullet.device.domain.dto.DeviceMappingUpdateDTO;
import com.wuweibi.bullet.entity.api.R;

public interface DeviceBiz {


    /**
     * 关闭用户所有映射
     * @param userId 用户Id
     */
    void closeAllMappingByUserId(Long userId);


    /**
     * 更新映射信息
     * @param deviceMappingDTO
     * @return
     */
    R updateMapping(DeviceMappingUpdateDTO deviceMappingDTO);
}
