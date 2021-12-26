package com.wuweibi.bullet.metrics.controller;


import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.metrics.domain.DataMetricsDTO;
import com.wuweibi.bullet.metrics.entity.DataMetrics;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import com.wuweibi.bullet.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 * 数据收集(DataMetrics)表控制层
 *
 * @author marker
 * @since 2021-11-07 14:17:51
 */
@RestController
@RequestMapping("/api/open/data/metrics")
public class DataMetricsOpenController {
    /**
     * 服务对象
     */
    @Resource
    private DataMetricsService dataMetricsService;
    @Resource
    private DeviceService deviceService;

    /**
     * 新增数据
     * @param dataMetrics 实体对象
     * @return 新增结果
     */
    @PostMapping()
    public R insert(@RequestBody @Valid DataMetricsDTO dataMetrics) {
        String deviceNo = dataMetrics.getDeviceNo();
        Device device = deviceService.getByDeviceNo(deviceNo);
        if (device == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_EXIST);
        }
        DataMetrics entity = new DataMetrics();
        BeanUtils.copyProperties(dataMetrics, entity);
        entity.setCreateTime(new Date());
        entity.setDeviceId(device.getId());
        entity.setUserId(device.getUserId());
        return R.success(this.dataMetricsService.save(entity));
    }

}
