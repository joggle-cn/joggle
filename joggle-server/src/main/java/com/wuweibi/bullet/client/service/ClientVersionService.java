package com.wuweibi.bullet.client.service;

import com.wuweibi.bullet.client.entity.ClientVersion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.domain.dto.ClientInfoDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2021-08-12
 */
public interface ClientVersionService extends IService<ClientVersion> {

    /**
     * 获取新版本
     * @return
     * @param clientInfoDTO
     */
    ClientVersion getNewVersion(ClientInfoDTO clientInfoDTO);

}
