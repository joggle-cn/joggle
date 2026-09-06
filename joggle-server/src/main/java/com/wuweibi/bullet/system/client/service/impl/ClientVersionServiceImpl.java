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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
@Slf4j
@Service
public class ClientVersionServiceImpl extends ServiceImpl<ClientVersionMapper, ClientVersion> implements ClientVersionService {

    @Resource
    private AliOssProperties aliOssProperties;

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

    @Override
    public int updateChecksumByOsArch(String version, String os, String arch, String downloadUrl, String checksum, String type) {
        ClientVersion clientVersion = this.baseMapper.selectOne(Wrappers.<ClientVersion>lambdaQuery()
                .eq(ClientVersion::getOs, os)
                .eq(ClientVersion::getArch, arch)
                .eq(ClientVersion::getType, type)
                .last("limit 1")
        );
        if (clientVersion == null) return 0;

        String baseUrl = aliOssProperties.getPublicServerUrl();
        boolean dev = !SpringUtils.isProduction();
        if (dev) {
            baseUrl = "http://192.168.1.6";
        }
        downloadUrl = baseUrl + downloadUrl.replaceFirst("^https?://[^/]+", "");
        if (dev) {
            downloadUrl = downloadUrl.replaceFirst("^(https?://[^/]+/[^/]+)/(\\d+\\.\\d+(\\.\\d+)?)/", "$1/");
        }

        clientVersion.setDownloadUrl(downloadUrl);
        clientVersion.setChecksum(checksum);
        if ("JOGGLE_CLIENT".equals(type)) {
            clientVersion.setSignature(getSignature(downloadUrl));
        }
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

    private String getSignature(String downloadUrl) {
        String signatureUrl = appendSuffix(downloadUrl, "sig");
        log.info("获取客户端签名: {}", signatureUrl);
        try {
            URLConnection connection = new URL(signatureUrl).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                String signature = content.toString();
                log.info("获取客户端签名成功: {}, length={}", signatureUrl, signature.length());
                return signature;
            }
        } catch (IOException e) {
            log.error("获取客户端签名失败: {}", signatureUrl, e);
            throw new RuntimeException(String.format("获取签名失败: %s", signatureUrl), e);
        }
    }

    private String appendSuffix(String url, String suffix) {
        int queryIndex = url.indexOf('?');
        String path = queryIndex >= 0 ? url.substring(0, queryIndex) : url;
        String query = queryIndex >= 0 ? url.substring(queryIndex) : "";
        return path + "." + suffix + query;
    }
}
