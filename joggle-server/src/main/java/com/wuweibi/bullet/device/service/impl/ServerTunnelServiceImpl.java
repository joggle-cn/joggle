package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.vo.TunnelOption;
import com.wuweibi.bullet.device.mapper.ServerTunnelMapper;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道(ServerTunnel)表服务实现类
 *
 * @author makejava
 * @since 2022-04-28 21:27:37
 */
@Service("serverTunnelService")
public class ServerTunnelServiceImpl extends ServiceImpl<ServerTunnelMapper, ServerTunnel> implements ServerTunnelService {

    @Override
    public List<TunnelOption> getOptionList() {
        return this.baseMapper.selectOptionList();
    }

    @Override
    public List<ServerTunnel> getListEnable() {
        return this.baseMapper.selectList(Wrappers.emptyWrapper());
    }
}

