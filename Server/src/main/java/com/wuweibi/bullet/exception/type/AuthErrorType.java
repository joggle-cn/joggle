package com.wuweibi.bullet.exception.type;

import lombok.Getter;


/**
 * 授权类错误
 * @author marker
 */
@Getter
public enum AuthErrorType implements ErrorType {

    INVALID_REQUEST("040001", "无效请求"),
    INVALID_CLIENT("040002", "无效client_id"),
    INVALID_GRANT("040003", "无效授权"),
    INVALID_SCOPE("040004", "无效scope"),
    INVALID_TOKEN("040005", "无效token"),
    INVALID_LOGIN("040006", "登录失效"),
    INSUFFICIENT_SCOPE("040010", "授权不足"),
    REDIRECT_URI_MISMATCH("040020", "redirect url不匹配"),
    ACCESS_DENIED("040030", "拒绝访问"),
    METHOD_NOT_ALLOWED("040040", "不支持该方法"),
    SERVER_ERROR("040050", "权限服务错误"),
    UNAUTHORIZED_CLIENT("040060", "未授权客户端"),
    UNAUTHORIZED("040061", "未授权"),
    UNSUPPORTED_RESPONSE_TYPE("040070", " 支持的响应类型"),
    UNSUPPORTED_GRANT_TYPE("040071", "不支持的授权类型"),
    NOT_BIND_USER("003024","未绑定openid" ),
    SMS_CODE_ERROR("040072","验证码错误" ),
    ACCOUNT_PASSWORD_ERROR("040073","账号或者密码错误" ),
    ACCOUNT_NOT_FOUND("040074", "用户不存在，请联系管理员！" )
    ;

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    AuthErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
