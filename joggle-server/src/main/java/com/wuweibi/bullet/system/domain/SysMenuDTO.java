package com.wuweibi.bullet.system.domain;

import com.wuweibi.bullet.entity.SysMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMenuDTO extends SysMenu {

    @ApiModelProperty("按钮列表")
    private String buttons = "[]";

}
