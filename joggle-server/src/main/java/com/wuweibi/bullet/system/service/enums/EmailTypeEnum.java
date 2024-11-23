package com.wuweibi.bullet.system.service.enums;


import lombok.Getter;

/**
 * 邮件枚举
 * @author marker
 */
@Getter
public enum EmailTypeEnum {
    DEVICE_DOWN("DEVICE_DOWN", "device_down_notice.htm",  "%s设备下线提醒"),
    USER_CERTIFICATION_NOTICE("USER_CERTIFICATION_NOTICE", "certification_result.ftl",  "Joggle实名认证结果"),
    VIP_EXPIRATION_NOTICE("VIP_EXPIRATION_NOTICE", "package_expiration_notice.htm",  "Joggle实名认证结果"),

    ;

    private String type;
    private String templateCode;
    private String defaultSubject;


      EmailTypeEnum(String type, String templateCode, String defaultSubject) {
        this.type = type;
        this.templateCode = templateCode;
        this.defaultSubject = defaultSubject;
    }
}
