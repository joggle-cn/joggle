package com.wuweibi.bullet.system.client.domain;

import lombok.Data;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class ClientVersionUpdateDTO {

    @Schema(description = "类型", example = "JOGGLE_CLIENT")
    private String type;

    @Schema(description = "版本更新项")
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(description = "checksum")
        private String checksum;

        @Schema(description = "os", example = "windows")
        private String os;

        @Schema(description = "arch", example = "amd64")
        private String arch;

        @Schema(description = "version", example = "0.1.15")
        private String version;

        @Schema(description = "downloadUrl")
        private String downloadUrl;

    }

}
