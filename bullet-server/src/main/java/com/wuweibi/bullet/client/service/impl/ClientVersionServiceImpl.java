package com.wuweibi.bullet.client.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wuweibi.bullet.client.entity.ClientVersion;
import com.wuweibi.bullet.client.mapper.ClientVersionMapper;
import com.wuweibi.bullet.client.service.ClientVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    public ClientVersion getNewVersion() {
        return this.baseMapper.selectOne(Wrappers.<ClientVersion>lambdaQuery()
                .eq(ClientVersion::getStatus, 1)
                .orderByDesc(ClientVersion::getCreateTime)
                .last("limit 1")
        );
    }
}
