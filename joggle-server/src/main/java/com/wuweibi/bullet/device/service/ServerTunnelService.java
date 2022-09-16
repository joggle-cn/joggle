package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.device.domain.dto.ServerTunnelAdminParam;
import com.wuweibi.bullet.device.domain.vo.ServerTunnelAdminVO;
import com.wuweibi.bullet.device.domain.vo.TunnelOption;
import com.wuweibi.bullet.device.entity.ServerTunnel;

import java.util.List;

/**
 * 通道(ServerTunnel)表服务接口
 *
 * @author makejava
 * @since 2022-04-28 21:27:35
 */
public interface ServerTunnelService extends IService<ServerTunnel> {

    List<TunnelOption> getOptionList();


    /**
     * 获取所有可用区域清单
     * @return
     */
    List<ServerTunnel> getListEnable();


    /**
     * 更新通道的在线状态
     * @param tunnelId 通道id
     * @param status 状态 1在线 0 不在线
     * @return
     */
    boolean updateStatus(Integer tunnelId, int status);


    /**
     * 分页查询通道
     * @param pageInfo 分页对象
     * @param params
     * @return
     */
    Page<ServerTunnelAdminVO> getAdminPage(Page pageInfo, ServerTunnelAdminParam params);
}

