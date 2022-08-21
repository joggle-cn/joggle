package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.entity.DeviceProxy;
import com.wuweibi.bullet.device.domain.DeviceProxyVO;
import com.wuweibi.bullet.device.domain.DeviceProxyParam;

/**
 * 设备代理(DeviceProxy)表服务接口
 *
 * @author marker
 * @since 2022-08-19 21:00:28
 */
public interface DeviceProxyService extends IService<DeviceProxy> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<DeviceProxyVO> getPage(Page pageInfo, DeviceProxyParam params);

    DeviceProxy getByDeviceId(Long deviceId);
}
