package com.wuweibi.bullet.business;

import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;

public interface DomainBiz {

    /**
     * 通过设备映射 获取可用的域名
     * @param deviceMapping
     * @return
     */
    R<Domain> getAvailableDomainByDeviceMapping(DeviceMapping deviceMapping);

}
