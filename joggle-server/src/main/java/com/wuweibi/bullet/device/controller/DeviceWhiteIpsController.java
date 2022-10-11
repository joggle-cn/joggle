package com.wuweibi.bullet.device.controller;


import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.DeviceWhiteIpsDTO;
import com.wuweibi.bullet.device.entity.DeviceWhiteIps;
import com.wuweibi.bullet.device.service.DeviceWhiteIpsService;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.wuweibi.bullet.protocol.Message.CONTROL_WHITE_IPS;

/**
 * (DeviceWhiteIps)表控制层
 *
 * @author marker
 * @since 2022-10-11 22:19:15
 */
@Slf4j
@RestController
@Api(value = "", tags = "")
@RequestMapping("/api/device/white-ips")
public class DeviceWhiteIpsController {
    /**
     * 服务对象
     */
    @Resource
    private DeviceWhiteIpsService deviceWhiteIpsService;


    /**
     * 通过主键查询单条数据
     *
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<DeviceWhiteIps> detail(@RequestParam Long deviceId) {
        DeviceWhiteIps entity = this.deviceWhiteIpsService.getByDeviceId(deviceId);
        if(entity == null){
            return R.fail("不存在");
        }
        return R.ok(entity);
    }

    @Resource
    private DeviceService deviceService;

    @Resource
    private WebsocketPool websocketPool;

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PutMapping
    public R<Boolean> save(@RequestBody @Valid DeviceWhiteIpsDTO dto) {
        Long deviceId = dto.getDeviceId();

        // TODO 校验ip的的地址正确性


        DeviceWhiteIps entity = this.deviceWhiteIpsService.getByDeviceId(deviceId);
        if (entity == null) {
            entity = new DeviceWhiteIps();
            entity.setDeviceId(dto.getDeviceId());
            entity.setIps(dto.getIps());
        }
        entity.setIps(dto.getIps());
        this.deviceWhiteIpsService.saveOrUpdate(entity);


        String[] ips = entity.getIps().split(";");
        if (ips.length == 0) {
            return R.ok();
        }

        DeviceDetail deviceDetail = deviceService.getDetail(deviceId);
        Bullet3Annotation annotation = websocketPool.getByTunnelId(deviceDetail.getServerTunnelId());
        if (annotation != null) {
            byte[] data = JSON.toJSONString(ips).getBytes();
            annotation.sendMessageBytes(CONTROL_WHITE_IPS, deviceDetail.getDeviceNo(), data);
        }
        return R.ok();
    }


}
