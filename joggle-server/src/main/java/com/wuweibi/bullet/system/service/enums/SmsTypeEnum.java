package com.wuweibi.bullet.system.service.enums;


public enum SmsTypeEnum {
    AUTH("AUTH", "SMS_251740093",  ""),
    LOGIN("LOGIN", " ",  "验证码：${code}，您正在登录，90秒内有效，验证码提供给他人可能导致账号被盗，请勿泄露，谨防被骗。"),
    DEVICE_DOWN("DEVICE_DOWN", "SMS_251740093",  "设备下线通知"),

    ;

    private String type;
    private String templateCode;
    private String description;

    public String getType() {
        return this.type;
    }

    public String getTemplateCode() {
        return this.templateCode;
    }


    public String getDescription() {
        return this.description;
    }

    private SmsTypeEnum(String type, String templateCode, String description) {
        this.type = type;
        this.templateCode = templateCode;
        this.description = description;
    }
}
