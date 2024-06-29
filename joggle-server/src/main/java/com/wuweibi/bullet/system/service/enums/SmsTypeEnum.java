package com.wuweibi.bullet.system.service.enums;


import lombok.Getter;

@Getter
public enum SmsTypeEnum {
    AUTH("AUTH", "您正在进行实名认证，您的验证码为 ${code} ，该验证码5分钟内有效，请勿泄露于他人。"),
    LOGIN("LOGIN", "验证码：${code}，您正在登录，90秒内有效，验证码提供给他人可能导致账号被盗，请勿泄露，谨防被骗。"),
    DEVICE_DOWN("DEVICE_DOWN", "您的设备 {deviceName}[{deviceNo}] 在{downTimeStr}已下线，IP:{publicIp}"),

    ;

    private String type;
    private String description;

    SmsTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
