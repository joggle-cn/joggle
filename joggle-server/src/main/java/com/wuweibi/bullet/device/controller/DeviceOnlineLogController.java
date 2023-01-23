package com.wuweibi.bullet.device.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogParam;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogVO;
import com.wuweibi.bullet.device.entity.DeviceOnlineLog;
import com.wuweibi.bullet.device.service.DeviceOnlineLogService;
import com.wuweibi.bullet.entity.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 设备在线日志(DeviceOnlineLog)表控制层
 *
 * @author marker
 * @since 2023-01-23 19:46:35
 */
@Slf4j
@RestController
@Api(value = "设备在线日志", tags = "设备在线日志")
@RequestMapping("/api/device/log")
public class DeviceOnlineLogController  {
    /**
     * 服务对象
     */
    @Resource
    private DeviceOnlineLogService deviceOnlineLogService;
    
    /**
     * 分页查询所有数据2
     *
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public R<Page<DeviceOnlineLogVO>> getPageList(PageParam page, DeviceOnlineLogParam params) {
        return R.ok(this.deviceOnlineLogService.getPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<DeviceOnlineLog> detail(@RequestParam Serializable id) {
        return R.ok(this.deviceOnlineLogService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param deviceOnlineLog 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public R<Boolean> save(@RequestBody DeviceOnlineLog deviceOnlineLog) {
        return R.ok(this.deviceOnlineLogService.save(deviceOnlineLog));
    }

    /**
     * 修改数据
     *
     * @param deviceOnlineLog 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R<Boolean> update(@RequestBody DeviceOnlineLog deviceOnlineLog) {
        return R.ok(this.deviceOnlineLogService.updateById(deviceOnlineLog));
    }


}
