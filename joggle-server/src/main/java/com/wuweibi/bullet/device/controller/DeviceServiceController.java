package com.wuweibi.bullet.device.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.business.DeviceBiz;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingDelDTO;
import com.wuweibi.bullet.device.domain.dto.DeviceScanDTO;
import com.wuweibi.bullet.device.domain.param.DeviceServiceParam;
import com.wuweibi.bullet.device.domain.vo.DeviceServiceVO;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.device.service.DeviceServiceService;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain2.mapper.DomainMapper;
import com.wuweibi.bullet.domain2.service.UserDomainService;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgDeviceScan;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>
 *  设备映射信息 前端控制器
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Slf4j
@Api(tags = "设备服务")
@WebApi
@RestController
@RequestMapping("/api/user/device/service")
public class DeviceServiceController {


    /** 端口映射服务 */
    @Resource
    private DeviceMappingService deviceMappingService;

    @Resource
    private WebsocketPool coonPool;


    /**
     * 删除映射关系
     * @return
     */
    @ApiOperation("删除映射")
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Object delete(@RequestBody @Valid DeviceMappingDelDTO dto){
        Long userId = SecurityUtils.getUserId();
        Long dmId = dto.getId();
        // 验证设备映射是自己的
        boolean status = deviceMappingService.exists(userId, dmId);
        if (status) {
            DeviceMapping entity = deviceMappingService.getById(dmId);
            deviceMappingService.removeById(dmId);

            DeviceDetail deviceDetail = deviceService.getDetail(entity.getDeviceId());
            if (deviceDetail == null) {
                return R.fail("设备不存在");
            }
            String deviceNo = deviceDetail.getDeviceNo();
            Bullet3Annotation annotation = coonPool.getByTunnelId(deviceDetail.getServerTunnelId());
            if (annotation != null) {
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
                annotation.sendMessage(deviceNo, msg);
            }
        }
        return MessageFactory.getOperationSuccess();
    }

    @Resource
    private DeviceServiceService deviceServiceService;



    /**
     * 开始内网扫描
     * @param dto 参数
     * @return
     */
    @ApiOperation("开始内网扫描")
    @PostMapping( "/start_scan")
    public R<Page<DeviceServiceVO>> startScan(@JwtUser Session session, @RequestBody DeviceScanDTO dto  ){
        String deviceNo = dto.getDeviceNo();
        Device device = deviceService.getByDeviceNo(deviceNo);
        if (Objects.isNull(device)) {
            return R.fail("设备不存在");
        }
        Integer serverTunnelId = device.getServerTunnelId();

        // 发送消息
        Bullet3Annotation annotation = coonPool.getByTunnelId(serverTunnelId);

        MsgDeviceScan msgDeviceScan = new MsgDeviceScan();
        annotation.sendMessage(deviceNo, msgDeviceScan);

        return R.success();
    }

    /**
     * 获取设备服务清单
     * @param params
     * @return
     */
    @ApiOperation("获取设备服务清单")
    @GetMapping( "/list")
    public R<Page<DeviceServiceVO>> getDeviceServiceList(@JwtUser Session session, PageParam pageParams,DeviceServiceParam params){
        Long userId = session.getUserId();
        params.setUserId(userId);
        Page pageP = new Page<DeviceServiceVO>(pageParams.getCurrent(), pageParams.getSize());
        Page<DeviceServiceVO> page = deviceServiceService.getListPage(pageP, params);
        return R.success(page);
    }

    @Resource
    private DomainMapper domainMapper;
    @Resource
    private UserFlowService userFlowService;
    @Resource
    private ServerTunnelService serverTunnelService;


    @Resource
    private DeviceBiz deviceBiz;
    @Resource
    private UserDomainService userDomainService;


    @Resource
    private DeviceService deviceService;



}
