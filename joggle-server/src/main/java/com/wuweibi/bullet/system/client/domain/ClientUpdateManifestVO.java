package com.wuweibi.bullet.system.client.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ClientUpdateManifestVO {

    @ApiModelProperty(value = "版本号", example = "0.1.18")
    private String version;

    @ApiModelProperty(value = "更新说明")
    private String notes;

    @ApiModelProperty(value = "发布时间", example = "2026-07-19T16:10:00Z")
    private String pubDate;

    @ApiModelProperty(value = "平台更新包信息")
    private Map<String, ManifestPlatform> platforms;

    @Data
    public static class ManifestPlatform {

        @ApiModelProperty(value = "下载地址")
        private String url;

        @ApiModelProperty(value = "Tauri 签名")
        private String signature;

    }
}
