package com.wuweibi.bullet.device.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.IdDTO;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.device.domain.DevicePeersConfigDTO;
import com.wuweibi.bullet.device.domain.DevicePeersDTO;
import com.wuweibi.bullet.device.domain.DevicePeersParam;
import com.wuweibi.bullet.device.domain.DevicePeersVO;
import com.wuweibi.bullet.device.entity.DevicePeers;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.res.manager.UserPackageLimitEnum;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.utils.IpAddrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Date;

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

    @Resource
    private UserPackageManager userPackageManager;
    /**
     * 新增数据
     *
     * @param dto 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    @Transactional
    public R<Boolean> save(@RequestBody @Valid DevicePeersDTO dto) {
        dto.setId(null);
        Long userId = SecurityUtils.getUserId();
        if (dto.getServerDeviceId().equals(dto.getClientDeviceId())) {
            return R.fail("请选择不同设备");
        }

        if (!IpAddrUtils.isIpv4(dto.getServerLocalHost())) {
            return R.fail("服务侧Host：请填写ipV4地址");
        }
        // 判断ip是内网ip
        if (!IpAddrUtils.internalIp(dto.getServerLocalHost())) {
            return R.fail("服务侧Host：请填写内网ip地址");
        }

        // 套餐设备数量限制校验
        if (!userPackageManager.checkLimit(userId, UserPackageLimitEnum.PeerNum, 1)) {
            return R.fail(SystemErrorType.PEER_BIND_ADD_ERROR);
        }

        // 校验设备
        if (!deviceService.existsUserDeviceId(userId, dto.getServerDeviceId())) {
            return R.fail("服务侧设备不存在");
        }
        if (!deviceService.existsUserDeviceId(userId, dto.getClientDeviceId())) {
            return R.fail("客户侧设备不存在");
        }

        // 校验客户端口唯一性
        if (this.devicePeersService.checkLocalPortDuplicate(dto.getClientDeviceId(), dto.getClientProxyPort(), dto.getId())) {
            return R.fail("代理端口存在冲突，请更换");
        }

        DevicePeers peers = this.devicePeersService.savePeers(userId, dto);
        R r1 = userPackageManager.usePackageAdd(userId, UserPackageLimitEnum.PeerNum, 1);
        if (r1.isFail()) {
            return r1;
        }

        DevicePeersConfigDTO dtoPeer = this.devicePeersService.getPeersConfig(peers.getId());
        if (dtoPeer.getClientDeviceTunnelId() == null || dtoPeer.getServerDeviceTunnelId() == null) {
            return R.fail("设备通道id错误，请确定设备状态");
        }
        // 发送peer消息
        devicePeersService.sendMsgPeerConfig(dtoPeer);

        return R.ok();
    }



    /**
     * 修改数据
     *
     * @param dto 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    @Transactional
    public R<Boolean> update(@RequestBody @Valid DevicePeersDTO dto) {
        Long userId = SecurityUtils.getUserId();
        if (dto.getId() == null) {
            return R.fail("id不能为空");
        }
        if (dto.getServerDeviceId().equals(dto.getClientDeviceId())) {
            return R.fail("请选择不同设备");
        }

        if (!IpAddrUtils.isIpv4(dto.getServerLocalHost())) {
            return R.fail("服务侧Host：请填写ipV4地址");
        }
        // 判断ip是内网ip
        if (!IpAddrUtils.internalIp(dto.getServerLocalHost())) {
            return R.fail("服务侧Host：请填写内网ip地址");
        }

        // 校验设备
        if (!deviceService.existsUserDeviceId(userId, dto.getServerDeviceId())) {
            return R.fail("服务侧设备不存在");
        }
        if (!deviceService.existsUserDeviceId(userId, dto.getClientDeviceId())) {
            return R.fail("客户侧设备不存在");
        }

        // 校验客户端口唯一性
        if (this.devicePeersService.checkLocalPortDuplicate(dto.getClientDeviceId(), dto.getClientProxyPort(), dto.getId())) {
            return R.fail("代理端口存在冲突，请更换");
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
        entity.setUpdateTime(new Date());
        entity.setClientMtu(dto.getClientMtu());
        entity.setServerMtu(dto.getClientMtu());
        entity.setConfigCompress(dto.getConfigCompress());
        this.devicePeersService.updateById(entity);

        DevicePeersConfigDTO devicePeersConfigDTO = this.devicePeersService.getPeersConfig(entity.getId());

        // 发送peer消息
        devicePeersService.sendMsgPeerConfig(devicePeersConfigDTO);


        return R.ok();
    }



    /**
     * 删除数据
     *
     * @param idDTO 主键
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping()
    @Transactional
    public R<Boolean> deleteById(@RequestBody @Valid IdDTO idDTO) {
        Integer id = idDTO.getId();
        Long userId = SecurityUtils.getUserId();
        DevicePeers devicePeers = devicePeersService.getById(id);
        if (devicePeers == null) {
            return R.fail("数据不存在");
        }
        if (devicePeers.getUserId().compareTo(userId) != 0) {
            return R.fail("用户数据不存在");
        }
        this.devicePeersService.removeById(idDTO.getId());
        R r1 = userPackageManager.usePackageAdd(userId, UserPackageLimitEnum.PeerNum, -1);
        if (r1.isFail()) {
            return r1;
        }
        return R.ok();
    }

}
