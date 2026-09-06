package com.wuweibi.bullet.system.domain;

import com.wuweibi.bullet.entity.SysMenu;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class SysMenuDTO extends SysMenu {

    @Schema(description = "按钮列表")
    private String buttons = "[]";

}
