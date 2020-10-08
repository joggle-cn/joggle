package com.wuweibi.bullet.exception.type;

import lombok.Getter;


/**
 * 自定义级别别错误码
 *
 * @author marker
 */
@Getter
public class CustomErrorType implements ErrorType {




    /**
     * 错误类型码
     */
    private String code;


    /**
     * 错误类型描述信息
     */
    private String msg;


    /**
     * 自定义错误类型
     * @param code 错误码
     * @param msg 消息
     */
    public CustomErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
