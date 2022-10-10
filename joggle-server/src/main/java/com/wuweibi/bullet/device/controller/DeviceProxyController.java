package com.wuweibi.bullet.device.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.DeviceProxyDTO;
import com.wuweibi.bullet.device.domain.DeviceProxyParam;
import com.wuweibi.bullet.device.domain.DeviceProxyVO;
import com.wuweibi.bullet.device.entity.DeviceProxy;
import com.wuweibi.bullet.device.service.DeviceProxyService;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.protocol.MsgProxy;
import com.wuweibi.bullet.protocol.domain.ProxyConfig;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Date;

/**
 * 设备代理(DeviceProxy)表控制层
 *
 * @author marker
 * @since 2022-08-19 21:00:28
 */
@Slf4j
@RestController
@Api(value = "设备代理", tags = "设备代理")
@RequestMapping("/api/device/proxy")
public class DeviceProxyController  {
    /**
     * 服务对象
     */
    @Resource
    private DeviceProxyService deviceProxyService;

    /**
     * 分页查询所有数据2
     *
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public R<Page<DeviceProxyVO>> getPageList(PageParam page, DeviceProxyParam params) {
        return R.ok(this.deviceProxyService.getPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<DeviceProxy> detail(@RequestParam Serializable id) {
        return R.ok(this.deviceProxyService.getById(id));
    }



    @Resource
    private WebsocketPool websocketPool;
    @Resource
    private DeviceService deviceService;

    /**
     * 新增数据
     *
     * @param  dto 实体对象
     * @return 新增结果
     */
    @ApiOperation("配置设备代理")
    @PutMapping("/config/one")
    public R<Boolean> saveOrUpdate(@RequestBody @Valid DeviceProxyDTO dto) {
        DeviceProxy entity = this.deviceProxyService.getByDeviceId(dto.getDeviceId());
        if (entity == null) {
            entity = new DeviceProxy();
            entity.setCreateTime(new Date());
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(new Date());
        this.deviceProxyService.saveOrUpdate(entity);

        DeviceDetail device = this.deviceService.getDetail(dto.getDeviceId());

        Bullet3Annotation annotation = websocketPool.getByTunnelId(device.getServerTunnelId());
        if (annotation != null) {
            ProxyConfig config = new ProxyConfig();
            config.setDeviceId(dto.getDeviceId());
            config.setProxyHost("0.0.0.0");
            config.setProxyPort(dto.getDeviceProxyPort());
            config.setType(dto.getType());
            config.setStatus(dto.getStatus());
            MsgProxy msg = new MsgProxy(config);
            annotation.sendMessage(device.getDeviceNo(), msg);

            // TODO 自动映射处理
        }
        return R.ok();
    }


}
