package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.entity.DeviceWhiteIps;
import com.wuweibi.bullet.device.domain.DeviceWhiteIpsVO;
import com.wuweibi.bullet.device.domain.DeviceWhiteIpsParam;

/**
 * (DeviceWhiteIps)表服务接口
 *
 * @author marker
 * @since 2022-10-11 22:19:15
 */
public interface DeviceWhiteIpsService extends IService<DeviceWhiteIps> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<DeviceWhiteIpsVO> getPage(Page pageInfo, DeviceWhiteIpsParam params);

    DeviceWhiteIps getByDeviceId(Long deviceId);

}
