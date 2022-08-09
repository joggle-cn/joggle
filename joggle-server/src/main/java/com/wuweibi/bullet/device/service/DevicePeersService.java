package com.wuweibi.bullet.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.domain.DevicePeersDTO;
import com.wuweibi.bullet.device.entity.DevicePeers;
import com.wuweibi.bullet.device.domain.DevicePeersVO;
import com.wuweibi.bullet.device.domain.DevicePeersParam;

/**
 * (DevicePeers)表服务接口
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
public interface DevicePeersService extends IService<DevicePeers> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<DevicePeersVO> getPage(Page pageInfo, DevicePeersParam params);



    boolean savePeers(Long userId, DevicePeersDTO dto);
}
