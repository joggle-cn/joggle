package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
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


}

