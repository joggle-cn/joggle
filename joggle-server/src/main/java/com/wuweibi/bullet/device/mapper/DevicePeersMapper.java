package com.wuweibi.bullet.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.domain.DevicePeersConfigDTO;
import com.wuweibi.bullet.device.domain.DevicePeersParam;
import com.wuweibi.bullet.device.entity.DevicePeers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (DevicePeers)表数据库访问层
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
public interface DevicePeersMapper extends BaseMapper<DevicePeers> {



    Page<DevicePeers> selectListPage(Page pageInfo, @Param("params") DevicePeersParam params);

    DevicePeersConfigDTO selectPeersConfig( @Param("id") Long id);

    /**
     * 根据设备号查询设备p2p配置
     * @param deviceNo 设备号
     * @return
     */
    List<DevicePeersConfigDTO> selectListByDeviceNo(@Param("deviceNo") String deviceNo);
}
