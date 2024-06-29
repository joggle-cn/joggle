package com.wuweibi.bullet.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogParam;
import com.wuweibi.bullet.device.domain.DeviceOnlineLogVO;
import com.wuweibi.bullet.device.entity.DeviceOnlineLog;
import org.apache.ibatis.annotations.Param;

/**
 * 设备在线日志(DeviceOnlineLog)表数据库访问层
 *
 * @author marker
 * @since 2023-01-23 19:46:35
 */
public interface DeviceOnlineLogMapper extends BaseMapper<DeviceOnlineLog> {


    /**
     * 设备上下线记录分页查询
     * @param pageInfo 分页信息
     * @param params 参数
     * @return
     */
    Page<DeviceOnlineLogVO> selectWebPage(Page pageInfo, @Param("params") DeviceOnlineLogParam params);

}
