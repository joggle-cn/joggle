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
    SYSTEM_BUSY("000000", "系统繁忙,请稍候再试"),
    FormFieldError("000001", "字段输入错误"),
    ACCOUNT_ACTIVATE_FAILD("000002", "账号激活失败"),
    CUSTOM_ERROR("000004", "操作失败"),
    OLDPASSWORD_ERROR("000005", "原密码错误"),
    ARGUMENT_NOT_VALID("000006", "参数校验失败"),
    LOGIN_INVALID("000007", "会话失效"),
    SYSTEN_FORM_ERROR("000008", "表单校验不通过"),
    DATA_NOT_FOUND("000009", "数据不存在"),

    /**
     *  1000 设备相关
     * ==================
     */
    DEVICE_INPUT_NUMBER("100001", "请输入设备编码"),
    DEVICE_OTHER_BIND("100002", "设备已经被其他账号绑定"),
    DEVICE_NOT_ONLINE("100003", "设备不在线"),
    DEVICE_NOT_EXIST("100004", "设备不存在"),
    DEVICE_SECRET_ERROR("100005", "设备秘钥错误"),
    DEVICE_BIND_LIMIT_ERROR("100006", "设备绑定限制"),
    PEER_BIND_ADD_ERROR("100007", "P2P隧道限制"),


    /**
     * 1001域名相关
      */
    DOMAIN_NOT_FOUND("100100", "域名找不到"),
    DOMAIN_IS_OTHER_BIND("100101", "域名已经被使用"),
    DOMAIN_IS_NOT_SUPPORT_ORDER("100102", "不支持续费"),
    DOMAIN_IS_DUE("100103", "域名已过期，请续费后启用！"),
    FLOW_IS_DUE("100104", "您的流量使用完了,咱不能操作映射！"),
    FLOW_IS_PAY_FAIL("100105", "流量扣取失败！"),



    /**
     * 1002 支付相关
      */
    PAY_MONEY_NOT_NULL("100200", "支付金额不能为空"),
    PAY_MONEY_BALANCE_NOT_ENOUGH("100201", "支付余额不足"),




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



    /**
     * 新建一个自定义的错误类型对象
     *
     * @param msg 消息
     * @return
     */
    public static ErrorType newErrorType(String msg) {
        return new CustomErrorType(CUSTOM_ERROR.code, msg);
    }
}
