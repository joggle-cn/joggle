package com.wuweibi.bullet.system.client.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClientVersionUpdateDTO {

    @ApiModelProperty(value = "类型", example = "JOGGLE_CLIENT")
    private String type;

    @ApiModelProperty(value = "版本更新项")
    private List<Item> items;

    @Data
    public static class Item {

        @ApiModelProperty(value = "checksum")
        private String checksum;

        @ApiModelProperty(value = "os", example = "windows")
        private String os;

        @ApiModelProperty(value = "arch", example = "amd64")
        private String arch;

        @ApiModelProperty(value = "version", example = "0.1.15")
        private String version;

        @ApiModelProperty(value = "downloadUrl")
        private String downloadUrl;

    }

}
