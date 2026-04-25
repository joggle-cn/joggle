package com.wuweibi.bullet.system.service.enums;


import lombok.Getter;

@Getter
public enum SystemConfigEnum {
    NOTICE_PHONES("NOTICE_PHONES", "通知手机号，逗号间隔"),
    NOTICE_ENABLE("NOTICE_ENABLE", "系统通知是否启用， true 启用  false 停用"),

    ;

    private String type;
    private String description;

    SystemConfigEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
