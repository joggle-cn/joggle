package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.entity.DeviceDoor;
import com.wuweibi.bullet.device.domain.DeviceDoorVO;
import com.wuweibi.bullet.device.domain.DeviceDoorParam;

/**
 * 设备任意门(DeviceDoor)表服务接口
 *
 * @author marker
 * @since 2022-08-03 21:21:27
 */
public interface DeviceDoorService extends IService<DeviceDoor> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<DeviceDoorVO> getPage(Page pageInfo, DeviceDoorParam params);


    /**
     * 查询设备任意门配置
     * @param deviceId 设备id
     * @return
     */
    DeviceDoor getByDeviceId(Long deviceId);
}
