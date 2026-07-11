package com.wuweibi.bullet.system.client.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClientVersionUpdateDTO {

    @ApiModelProperty(value = "类型", example = "JOGGLE_CLIENT")
    private String type;

    @ApiModelProperty(value = "表达式", example = "windows_amd64/joggle-client_0.1.15_x64-setup.exe:f2ca1bb6c7e907d06dafe4687e579fce76b37e4e93b7605022da52e6ccc26fd2:0.1.15")
    private String expression;

}
