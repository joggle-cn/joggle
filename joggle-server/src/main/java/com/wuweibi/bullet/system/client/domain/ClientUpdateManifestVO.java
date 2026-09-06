package com.wuweibi.bullet.system.client.domain;

import lombok.Data;

import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class ClientUpdateManifestVO {

    @Schema(description = "版本号", example = "0.1.18")
    private String version;

    @Schema(description = "更新说明")
    private String notes;

    @Schema(description = "发布时间", example = "2026-07-19T16:10:00Z")
    private String pubDate;

    @Schema(description = "平台更新包信息")
    private Map<String, ManifestPlatform> platforms;

    @Data
    public static class ManifestPlatform {

        @Schema(description = "下载地址")
        private String url;

        @Schema(description = "Tauri 签名")
        private String signature;

    }
}
