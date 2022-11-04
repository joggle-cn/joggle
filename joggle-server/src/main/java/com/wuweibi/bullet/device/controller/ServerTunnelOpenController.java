package com.wuweibi.bullet.device.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.device.domain.vo.ServerTunnelNodeVO;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.utils.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 通道(ServerTunnel)表控制层
 *
 * @author makejava
 * @since 2022-04-28 21:27:30
 */
@WebApi
@Api(tags = "通道管理")
@RestController
@RequestMapping("/api/open/server/tunnel")
public class ServerTunnelOpenController {
    /**
     * 服务对象
     */
    @Resource
    private ServerTunnelService serverTunnelService;

    /**
     * 通道节点列表
     *
     * @return 所有数据
     */
    @ApiOperation("通道节点列表")
    @GetMapping("/node/list")
    public R<Page<ServerTunnelNodeVO>> getNodeList(PageParam pageParam) {
        Page<ServerTunnelNodeVO> page = this.serverTunnelService.getNodeStatusList(pageParam.toMybatisPlusPage());
        long nowTime = new Date().getTime();
        page.getRecords().forEach(item->{
            if(item.getServerUpTime() == null){
                item.setOnlineTime("-");
                return;
            }
            Date time = item.getServerUpTime().compareTo(item.getServerDownTime()) >=0?item.getServerDownTime(): item.getServerUpTime();
            if (time == null) {
                time = new Date();
            }
            String subTime =  DateTimeUtil.diffDate(time.getTime(), nowTime);
            item.setOnlineTime(subTime);
        });
        return R.ok(page);
    }

}

