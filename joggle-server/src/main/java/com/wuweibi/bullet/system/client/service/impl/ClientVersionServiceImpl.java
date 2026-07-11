package com.wuweibi.bullet.system.client.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.alias.CacheBlock;
import com.wuweibi.bullet.config.properties.AliOssProperties;
import com.wuweibi.bullet.domain.dto.ClientInfoDTO;
import com.wuweibi.bullet.system.client.domain.ClientVersionAdminListVO;
import com.wuweibi.bullet.system.client.domain.NgrokVersionVO;
import com.wuweibi.bullet.system.client.entity.ClientVersion;
import com.wuweibi.bullet.system.client.mapper.ClientVersionMapper;
import com.wuweibi.bullet.system.client.service.ClientVersionService;
import com.wuweibi.bullet.system.domain.dto.ClientVersionParam;
import com.wuweibi.bullet.utils.SpringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
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
                .eq(ClientVersion::getType, clientInfoDTO.getApp_id())
                .eq(ClientVersion::getStatus, 1)
                .eq(ClientVersion::getOs, clientInfoDTO.getOs())
                .eq(ClientVersion::getArch, clientInfoDTO.getArch())
                .orderByDesc(ClientVersion::getCreateTime)
                .last("limit 1")
        );
    }

    @Resource
    private AliOssProperties aliOssProperties;

    @Override
    public int updateChecksumByOsArch(String version, String os, String arch, String binFilePath, String checksum, String type, String signature) {
        ClientVersion clientVersion = this.baseMapper.selectOne(Wrappers.<ClientVersion>lambdaQuery()
                .eq(ClientVersion::getOs, os)
                .eq(ClientVersion::getArch, arch)
                .eq(ClientVersion::getType, type)
                .eq(ClientVersion::getStatus, 1)
        );
        if (clientVersion == null) return 0;

        // 生成下载URL
        String baseUrl = aliOssProperties.getPublicServerUrl();
        if (!SpringUtils.isProduction()) {
            baseUrl = "http://192.168.1.6";
        }
        String downloadURL;
        if ("JOGGLE_CLIENT".equals(type)) {
            String filename = binFilePath.substring(binFilePath.lastIndexOf("/") + 1);
            downloadURL = String.format("%s/joggle-client/%s", baseUrl, filename);
        } else {
            downloadURL = String.format("%s/client/%s/%s", baseUrl, version, binFilePath);
        }
        clientVersion.setDownloadUrl(downloadURL);
        clientVersion.setChecksum(checksum);
        clientVersion.setSignature(signature);
        clientVersion.setTitle(String.format("joggle-%s-%s", type.toLowerCase(), version));
        clientVersion.setVersion(version);
        clientVersion.setStatus(true);
        clientVersion.setUpdateTime(new Date());
        return this.baseMapper.updateById(clientVersion);
    }

    @Override
    @Cacheable(cacheNames = CacheBlock.CACHE_VERSION_DETAIL, key = "'version'")
    public NgrokVersionVO getMaxVersion() {
        NgrokVersionVO versionVO = this.baseMapper.selectMaxVersion();
        if (versionVO == null) {
            versionVO = new NgrokVersionVO();
        }
        versionVO.setClientVersion(String.format("v%s", versionVO.getClientVersion()));
        versionVO.setServerVersion(String.format("v%s", versionVO.getServerVersion()));
        return versionVO;
    }

    @Override
    public Page<ClientVersionAdminListVO> getAdminList(Page pageInfo, ClientVersionParam params) {
        return this.baseMapper.selectAdminList(pageInfo, params);
    }

    @Override
    public ClientVersion getLatestVersion(String os, String arch) {
        return this.baseMapper.selectOne(Wrappers.<ClientVersion>lambdaQuery()
                .eq(ClientVersion::getType, "JOGGLE_CLIENT")
                .eq(ClientVersion::getOs, os)
                .eq(ClientVersion::getArch, arch)
                .eq(ClientVersion::getStatus, 1)
                .orderByDesc(ClientVersion::getCreateTime)
                .last("limit 1")
        );
    }

    @Override
    public List<ClientVersion> getUpdateManifestList() {
        return this.baseMapper.selectList(Wrappers.<ClientVersion>lambdaQuery()
                .eq(ClientVersion::getType, "JOGGLE_CLIENT")
                .eq(ClientVersion::getStatus, 1)
        );
    }
}
