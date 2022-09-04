package com.wuweibi.bullet.device.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.device.domain.vo.ServerTunnelVO;
import com.wuweibi.bullet.device.domain.vo.TunnelOption;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.entity.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通道(ServerTunnel)表控制层
 *
 * @author makejava
 * @since 2022-04-28 21:27:30
 */
@WebApi
@Api(tags = "通道管理")
@RestController
@RequestMapping("/api/server/tunnel")
public class ServerTunnelController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ServerTunnelService serverTunnelService;


    /**
     * 设备列表
     *
     * @return
     */
    @ApiOperation("通道区域下拉列表")
    @GetMapping("/options")
    public R<List<TunnelOption>> deviceOptions( ) {
        List<TunnelOption> list = serverTunnelService.getOptionList();
        return R.ok(list);
    }

    /**
     * 获取所有数据
     * @return 所有数据
     */
    @GetMapping("/list")
    public R<List<ServerTunnelVO>> selectAll() {
        List<ServerTunnel> list = this.serverTunnelService.list();
        return R.success(list.stream().map(item->{
            ServerTunnelVO vo = new ServerTunnelVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList()));
    }

}

