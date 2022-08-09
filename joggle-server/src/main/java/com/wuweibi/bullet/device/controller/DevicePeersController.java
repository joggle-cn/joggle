package com.wuweibi.bullet.device.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.device.domain.DevicePeersDTO;
import com.wuweibi.bullet.device.domain.DevicePeersParam;
import com.wuweibi.bullet.device.domain.DevicePeersVO;
import com.wuweibi.bullet.device.entity.DevicePeers;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgPeer;
import com.wuweibi.bullet.protocol.domain.PeerConfig;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;

/**
 * (DevicePeers)表控制层
 * p2p映射
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@Slf4j
@AdminApi
@RestController
@Api(value = "p2p映射", tags = "p2p映射")
@RequestMapping("/api/device/peers")
public class DevicePeersController {
    /**
     * 服务对象
     */
    @Resource
    private DevicePeersService devicePeersService;

    /**
     * 分页查询所有数据2
     *
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public R<Page<DevicePeersVO>> getPageList(PageParam page, DevicePeersParam params) {
        Long userId = SecurityUtils.getUserId();
        params.setUserId(userId);

        return R.ok(this.devicePeersService.getPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<DevicePeers> detail(@RequestParam Serializable id) {
        DevicePeers entity = this.devicePeersService.getById(id);
        if (entity == null) {
            return R.fail(SystemErrorType.DATA_NOT_FOUND);
        }
        return R.ok(entity);
    }

    @Resource
    private DeviceService deviceService;

    /**
     * 新增数据
     *
     * @param dto 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public R<Boolean> save(@RequestBody @Valid DevicePeersDTO dto) {
        dto.setId(null);
        Long userId = SecurityUtils.getUserId();
        if (dto.getServerDeviceId().equals(dto.getClientDeviceId())) {
            return R.fail("请选择不同设备");
        }

        // 校验设备
        if (!deviceService.existsDeviceId(dto.getServerDeviceId())) {
            return R.fail("服务侧设备不存在");
        }
        if (!deviceService.existsDeviceId(dto.getClientDeviceId())) {
            return R.fail("客户侧设备不存在");
        }

        Long peerMappingId = this.devicePeersService.savePeers(userId, dto);


        Device deviceServer = deviceService.getById(dto.getServerDeviceId());
        Device deviceClient = deviceService.getById(dto.getClientDeviceId());


        String appName = DigestUtils.md5Hex(String.valueOf(peerMappingId));

        // 服务侧发送peer消息
        sendMsgPeerConfig(dto, deviceServer, appName, PeerConfig.SERVER);
        // 客户侧发送peer消息
        sendMsgPeerConfig(dto, deviceClient, appName, PeerConfig.CLIENT);

        return R.ok();
    }

    private void sendMsgPeerConfig(DevicePeersDTO dto, Device deviceServer, String appName, String type) {
        String deviceNo = deviceServer.getDeviceNo();
        BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
        if (annotation != null) {
            PeerConfig doorConfig = new PeerConfig();
            doorConfig.setAppName(appName);
            doorConfig.setPort(dto.getClientProxyPort());
            if (PeerConfig.SERVER.equals(type)) {
                doorConfig.setPort(dto.getServerLocalPort());
            }
            doorConfig.setType(type);
            doorConfig.setEnable(dto.getStatus());
            JSONObject data = (JSONObject) JSON.toJSON(doorConfig);
            MsgPeer msg = new MsgPeer(data.toJSONString());
            annotation.sendMessage(msg);
        }
    }

    @Resource
    private CoonPool coonPool;

    /**
     * 修改数据
     *
     * @param dto 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R<Boolean> update(@RequestBody @Valid DevicePeersDTO dto) {
        Long userId = SecurityUtils.getUserId();
        if (dto.getId() == null) {
            return R.fail("id不能为空");
        }
        if (dto.getServerDeviceId().equals(dto.getClientDeviceId())) {
            return R.fail("请选择不同设备");
        }
        // 校验设备
        if (!deviceService.existsDeviceId(dto.getServerDeviceId())) {
            return R.fail("服务侧设备不存在");
        }
        if (!deviceService.existsDeviceId(dto.getClientDeviceId())) {
            return R.fail("客户侧设备不存在");
        }
        DevicePeers entity = devicePeersService.getById(dto.getId());
        if (entity == null) {
            return R.fail("数据不存在");
        }
        if (!entity.getUserId().equals(userId)) {
            return R.fail("数据不存在");
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        entity.setUpdateTime(entity.getCreateTime());


        this.devicePeersService.updateById(entity);

        String appName = entity.getAppName();

        Device deviceServer = deviceService.getById(dto.getServerDeviceId());
        Device deviceClient = deviceService.getById(dto.getClientDeviceId());

        // 服务侧发送peer消息
        sendMsgPeerConfig(dto, deviceServer, appName, PeerConfig.SERVER);
        // 客户侧发送peer消息
        sendMsgPeerConfig(dto, deviceClient, appName, PeerConfig.CLIENT);

        return R.ok();
    }


//    /**
//     * 删除数据
//     *
//     * @param idDTO 主键
//     * @return 删除结果
//     */
//    @ApiOperation("删除数据")
//    @DeleteMapping()
//    public R<Boolean> deleteById(@RequestBody @Valid IdDTO idDTO) {
//        return R.ok(this.devicePeersService.removeById(idDTO.getId()));
//    }

}
