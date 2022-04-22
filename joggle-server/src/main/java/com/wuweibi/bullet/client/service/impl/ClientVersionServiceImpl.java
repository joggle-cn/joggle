package com.wuweibi.bullet.client.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wuweibi.bullet.client.entity.ClientVersion;
import com.wuweibi.bullet.client.mapper.ClientVersionMapper;
import com.wuweibi.bullet.client.service.ClientVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.domain.dto.ClientInfoDTO;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author marker
 * @since 2021-08-12
 */
@Service
public class ClientVersionServiceImpl extends ServiceImpl<ClientVersionMapper, ClientVersion> implements ClientVersionService {

    @Override
    public ClientVersion getNewVersion(ClientInfoDTO clientInfoDTO) {
        return this.baseMapper.selectOne(Wrappers.<ClientVersion>lambdaQuery()
                .eq(ClientVersion::getStatus, 1)
                .eq(ClientVersion::getOs, clientInfoDTO.getOs())
                .eq(ClientVersion::getArch, clientInfoDTO.getArch())
                .orderByDesc(ClientVersion::getCreateTime)
                .last("limit 1")
        );
    }

    @Override
    public int updateChecksumByOsArch(String os, String arch, String checksum) {
        ClientVersion clientVersion = this.baseMapper.selectOne(Wrappers.<ClientVersion>lambdaQuery()
                .eq(ClientVersion::getStatus, 1)
                .eq(ClientVersion::getOs, os)
                .eq(ClientVersion::getArch, arch));
        if(clientVersion == null) return 0;

        clientVersion.setChecksum(checksum);
        clientVersion.setUpdateTime(new Date());
        return this.baseMapper.updateById(clientVersion);
    }
}
