package com.wuweibi.bullet.system.client.controller;

import com.wuweibi.bullet.config.properties.AliOssProperties;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.system.client.domain.ClientUpdateManifestVO;
import com.wuweibi.bullet.system.client.domain.ClientUpdateManifestVO.ManifestPlatform;
import com.wuweibi.bullet.system.client.domain.ClientVersionLatestVO;
import com.wuweibi.bullet.system.client.entity.ClientVersion;
import com.wuweibi.bullet.system.client.service.ClientVersionService;
import com.wuweibi.bullet.utils.SpringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/open/client/version")
@Tag(name = "客户端版本公开接口")
public class ClientVersionOpenController {

    @Resource
    private ClientVersionService clientVersionService;

    @Resource
    private AliOssProperties aliOssProperties;

    @GetMapping("/latest")
    @Operation(summary = "获取客户端最新版本")
    
    public R<ClientVersionLatestVO> latest(
            @Parameter(description = "操作系统", required = true, schema = @Schema(allowableValues = {"windows", "darwin", "linux"}))
            @RequestParam String os,
            @Parameter(description = "处理器架构", required = true, schema = @Schema(allowableValues = {"x64", "arm64", "x86"}))
            @RequestParam String arch) {
        ClientVersion clientVersion = clientVersionService.getLatestVersion(os, arch);
        if (clientVersion == null) {
            return R.fail(SystemErrorType.CLIENT_VERSION_NOT_FOUND);
        }

        ClientVersionLatestVO vo = new ClientVersionLatestVO();
        vo.setVersion(clientVersion.getVersion());
        vo.setTitle(clientVersion.getTitle());
        vo.setDescription(clientVersion.getDescription());
        vo.setDownloadUrl(clientVersion.getDownloadUrl());
        vo.setChecksum(clientVersion.getChecksum());
        vo.setOs(clientVersion.getOs());
        vo.setArch(clientVersion.getArch());
        vo.setCreateTime(clientVersion.getCreateTime());
        return R.success(vo);
    }

    @GetMapping("/update-manifest")
    @Operation(summary = "Tauri 更新器获取更新清单")
    
    public ClientUpdateManifestVO updateManifest(
            @Parameter(description = "当前版本号")
            @RequestParam(required = false) String currentVersion) {
        ClientUpdateManifestVO vo = new ClientUpdateManifestVO();
        List<ClientVersion> list = clientVersionService.getUpdateManifestList();
        if (list.isEmpty()) {
            return vo;
        }
        ClientVersion first = list.get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        vo.setVersion(first.getVersion());
        vo.setPubDate(sdf.format(first.getCreateTime()));
        vo.setNotes(first.getDescription());

        String baseUrl = aliOssProperties.getPublicServerUrl();
        if (!SpringUtils.isProduction()) {
            baseUrl = "http://192.168.1.6";
        }
        Map<String, ManifestPlatform> platforms = new HashMap<>();
        for (ClientVersion cv : list) {
            String key = buildPlatformKey(cv.getOs(), cv.getArch());
            String urlPath = cv.getDownloadUrl().replaceFirst("^https?://[^/]+", "");
            ManifestPlatform mp = new ManifestPlatform();
            mp.setUrl(baseUrl + urlPath);
            mp.setSignature(cv.getSignature());
            platforms.put(key, mp);
        }
        vo.setPlatforms(platforms);
        return vo;
    }

    private String buildPlatformKey(String os, String arch) {
        if ("amd64".equals(arch)) return os + "-x86_64";
        if ("arm64".equals(arch)) return os + "-aarch64";
        if ("386".equals(arch)) return os + "-i686";
        return os + "-" + arch;
    }
}
