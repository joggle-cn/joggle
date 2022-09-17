package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.dto.ServerTunnelAdminParam;
import com.wuweibi.bullet.device.domain.vo.ServerTunnelAdminVO;
import com.wuweibi.bullet.device.domain.vo.TunnelOption;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.mapper.ServerTunnelMapper;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道(ServerTunnel)表服务实现类
 *
 * @author makejava
 * @since 2022-04-28 21:27:37
 */
@Service
public class ServerTunnelServiceImpl extends ServiceImpl<ServerTunnelMapper, ServerTunnel> implements ServerTunnelService {

    @Override
    public List<TunnelOption> getOptionList() {
        return this.baseMapper.selectOptionList();
    }

    @Override
    public List<ServerTunnel> getListEnable() {
        return this.baseMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public boolean updateStatus(Integer tunnelId, int status) {
        return this.update(Wrappers.<ServerTunnel>lambdaUpdate()
                .eq(ServerTunnel::getId, tunnelId)
                .set(ServerTunnel::getStatus, status)
                );
    }

    @Override
    public Page<ServerTunnelAdminVO> getAdminPage(Page pageInfo, ServerTunnelAdminParam params) {
        Page<ServerTunnelAdminVO> page = this.baseMapper.selectAdminPage(pageInfo, params);
        page.getRecords().forEach(item->{
            item.setName("["+item.getCountry()+"|"+item.getArea()+"] "+item.getName());
        });
        return page;
    }
}

