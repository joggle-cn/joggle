package com.wuweibi.bullet.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.device.domain.dto.ServerTunnelAdminParam;
import com.wuweibi.bullet.device.domain.vo.ServerTunnelAdminVO;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.entity.api.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 通道(TServerTunnel)表控制层
 *
 * @author marker
 * @since 2022-09-16 20:45:17
 */
@RestController
@RequestMapping("/admin/server/tunnel")
public class ServerTunnelAdminController {
    /**
     * 服务对象
     */
    @Resource
    private ServerTunnelService serverTunnelService;

    /**
     * 通道分页查询
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("通道分页查询")
    @GetMapping("/list")
    public R<Page<ServerTunnelAdminVO>> getPageList(PageParam page, ServerTunnelAdminParam params) {
        return R.ok(this.serverTunnelService.getAdminPage(page.toMybatisPlusPage(), params));
    }



}
