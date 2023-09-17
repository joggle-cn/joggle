package com.wuweibi.bullet.domain2.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain2.domain.DomainAdminParam;
import com.wuweibi.bullet.domain2.domain.dto.DomainUpdateDTO;
import com.wuweibi.bullet.domain2.domain.dto.ReleaseResourceDTO;
import com.wuweibi.bullet.domain2.domain.vo.DomainDetailAdminVO;
import com.wuweibi.bullet.domain2.domain.vo.DomainListVO;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.domain2.enums.DomainTypeEnum;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.protocol.MsgTunnelConfig;
import com.wuweibi.bullet.protocol.domain.DomainConfig;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 我的域名接口
 *
 * @author marker
 * @create 2019-12-26 下午9:19
 **/
@Slf4j
@AdminApi
@RestController
@RequestMapping("/admin/domain")
public class DomainAdminController {


    @Resource
    private WebsocketPool websocketPool;


    /**
     * 域名管理
     */
    @Resource
    private DomainService domainService;
    @Resource
    private ServerTunnelService serverTunnelService;

    @Resource
    private DeviceMappingService deviceMappingService;



    /**
     * 域名分页查询
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("域名分页查询")
    @GetMapping("/list")
    public R<Page<DomainListVO>> getAdminList(PageParam page, DomainAdminParam params) {
        return R.ok(this.domainService.getAdminList(page.toMybatisPlusPage(), params));
    }


    /**
     * 获取我的域名信息
     */
    @ApiModelProperty("获取域名信息")
    @GetMapping(value = "/detail")
    public R<DomainDetailAdminVO> getInfo(@RequestParam("id") Long domainId) {
        Domain domain = domainService.getById(domainId);
        if (domain == null) {
            return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
        }

        ServerTunnel serverTunnel = serverTunnelService.getById(domain.getServerTunnelId());
        if (serverTunnel == null) {
            return R.fail("域名/端口信息有误");
        }
        DomainDetailAdminVO domainDetailVO = new DomainDetailAdminVO();
        BeanUtils.copyProperties(domain, domainDetailVO);
        domainDetailVO.setDomainFull(domain.getDomain());
        if (domain.getType().equals(DomainTypeEnum.DOMAIN.getType())) {
            domainDetailVO.setDomainFull(String.format("%s.%s", domain.getDomain(), serverTunnel.getServerAddr()));
        } else {
            domainDetailVO.setDomainFull(String.format("%s:%s", serverTunnel.getServerAddr(), domain.getDomain()));
        }
        return R.success(domainDetailVO);
    }

    @Resource
    private WebsocketPool coonPool;


    @Resource
    private DeviceService deviceService;

    /**
     * 更新域名信息
     *
     * @return
     */
    @ApiOperation("更新域名信息")
    @PutMapping("/")
    public R updateDomainInfo(@RequestBody @Valid DomainUpdateDTO dto) {
        Long userId = SecurityUtils.getUserId();
        Long domainId = dto.getId();
        if(!domainService.exists(dto.getId())){
            return R.fail("域名不存在");
        }
        Domain domain = domainService.getById(domainId);
        domain.setStatus(dto.getStatus());
        domain.setBandwidth(dto.getBandwidth());
        domain.setConcurrentNum(dto.getConcurrentNum());
        domain.setDueTime(dto.getDueTime());
        domain.setSalesPrice(dto.getSalesPrice());
        domain.setOriginalPrice(dto.getOriginalPrice());
        domainService.updateById(domain);

        // 如果宽带信息发生了更变

        // 根据域名id查询映射
        DeviceMapping deviceMapping = deviceMappingService.getByDomainId(null, domainId);
        if (deviceMapping == null) {
            log.info("域名id={} 设备映射不存在", domainId);
            return R.ok();
        }

        DeviceDetail deviceDetail = deviceService.getDetail(deviceMapping.getDeviceId());
        if (deviceDetail == null) {
            return R.fail("设备不存在");
        }
        String deviceNo = deviceDetail.getDeviceNo();

        // 通知到对应的通道
        Bullet3Annotation annotation = coonPool.getByTunnelId(domain.getServerTunnelId());
        if (annotation != null) {
            DomainConfig domainConfig = new DomainConfig();
            domainConfig.setMapId(deviceMapping.getId());
            domainConfig.setDeviceNo(deviceNo);
            domainConfig.setConcurrentNum(domain.getConcurrentNum());
            domainConfig.setBandwidth(domain.getBandwidth());
            MsgTunnelConfig tunnelConfig = new MsgTunnelConfig((JSONObject) JSON.toJSON(domainConfig));
            annotation.sendMessageToServer(tunnelConfig);
        }
        return R.success();
    }



    /**
     * 搜索可购买的域名
     */
    @ApiOperation(value = "发放域名", tags="后台")
    @PostMapping("/release")
    public R<Boolean> releaseDomain(@RequestBody @Valid ReleaseResourceDTO dto) {
        // 校验参数
        if (!ArrayUtils.contains(new int[]{1, 2}, dto.getType())) {
            return R.fail("资源类型错误");
        }

        // 校验通道师傅存在
        ServerTunnel serverTunnel = serverTunnelService.getById(dto.getServerTunnelId());
        if (serverTunnel == null) {
            return R.fail("服务器通道不存在");
        }

        boolean status = domainService.release(dto);
        return R.ok(status);
    }



}
