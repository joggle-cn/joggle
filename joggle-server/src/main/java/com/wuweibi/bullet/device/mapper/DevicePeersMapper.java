package com.wuweibi.bullet.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.domain.DevicePeersParam;
import com.wuweibi.bullet.device.entity.DevicePeers;
import org.apache.ibatis.annotations.Param;

/**
 * (DevicePeers)表数据库访问层
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
public interface DevicePeersMapper extends BaseMapper<DevicePeers> {



    Page<DevicePeers> selectListPage(Page pageInfo, @Param("params") DevicePeersParam params);
}
