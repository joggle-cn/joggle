package com.wuweibi.bullet.exception.type;

import lombok.Getter;


/**
 * 系统错误定义
 * 业务错误代码
 *
 * @author marker
 */
@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("100000", "系统繁忙,请稍候再试"),

    /**
     *  1000 设备相关
     * ==================
     */
    DEVICE_INPUT_NUMBER("100001", "请输入设备编码"),
    DEVICE_OTHER_BIND("100002", "设备已经被其他账号绑定"),
    DEVICE_NOT_ONLINE("100003", "设备不在线"),
    DEVICE_NOT_EXIST("100004", "设备不存在"),


    /**
     * 1001域名相关
      */
    DOMAIN_NOT_FOUND("100100", "域名找不到"),




    ;

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    SystemErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
