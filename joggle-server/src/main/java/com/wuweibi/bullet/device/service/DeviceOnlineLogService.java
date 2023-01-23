package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.entity.DeviceOnlineLog;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogVO;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogParam;

/**
 * 设备在线日志(DeviceOnlineLog)表服务接口
 *
 * @author marker
 * @since 2023-01-23 19:46:35
 */
public interface DeviceOnlineLogService extends IService<DeviceOnlineLog> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<DeviceOnlineLogVO> getPage(Page pageInfo, DeviceOnlineLogParam params);
}
