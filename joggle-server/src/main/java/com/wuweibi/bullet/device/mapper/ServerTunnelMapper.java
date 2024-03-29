package com.wuweibi.bullet.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.domain.dto.ServerTunnelAdminParam;
import com.wuweibi.bullet.device.domain.vo.ServerTunnelAdminVO;
import com.wuweibi.bullet.device.domain.vo.ServerTunnelNodeVO;
import com.wuweibi.bullet.device.domain.vo.TunnelOption;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通道(ServerTunnel)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-28 21:27:30
 */
public interface ServerTunnelMapper extends BaseMapper<ServerTunnel> {

    List<TunnelOption> selectOptionList();

    Page<ServerTunnelAdminVO> selectAdminPage(Page pageInfo,@Param("params") ServerTunnelAdminParam params);

    /**
     * 分页查询节点状态
     * @param pageInfo
     * @return
     */
    Page<ServerTunnelNodeVO> selectNodeStatusList(Page pageInfo);

    /**
     * 检查域名是否存在
     * @param baseDomain 一级域名
     * @return
     */
    boolean checkDomain(String baseDomain);
}

