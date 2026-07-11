package com.wuweibi.bullet.system.client.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ClientUpdateManifestVO {

    private String version;
    private String notes;
    private String pubDate;
    private Map<String, ManifestPlatform> platforms;

    @Data
    public static class ManifestPlatform {
        private String url;
        private String signature;
    }
}
