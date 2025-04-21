package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.device.domain.param.DeviceServiceParam;
import com.wuweibi.bullet.device.domain.vo.DeviceServiceVO;
import com.wuweibi.bullet.entity.DeviceMapping;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceServiceService extends IService<DeviceMapping> {



    Page<DeviceServiceVO> getListPage(Page pageParams, DeviceServiceParam params);
}
