package com.wuweibi.bullet.system.service.enums;


import lombok.Getter;

/**
 * 邮件枚举
 * @author marker
 */
@Getter
public enum EmailTypeEnum {
    DEVICE_DOWN("DEVICE_DOWN", "device_down_notice.htm",  "%s设备下线提醒"),

    ;

    private String type;
    private String templateCode;
    private String subject;


      EmailTypeEnum(String type, String templateCode, String subject) {
        this.type = type;
        this.templateCode = templateCode;
        this.subject = subject;
    }
}
