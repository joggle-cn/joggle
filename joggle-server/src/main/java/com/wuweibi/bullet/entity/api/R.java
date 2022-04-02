package com.wuweibi.bullet.entity.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wuweibi.bullet.domain.domain.LayPage;
import com.wuweibi.bullet.domain.message.FormFieldMessage;
import com.wuweibi.bullet.entity.Role;
import com.wuweibi.bullet.exception.BaseException;
import com.wuweibi.bullet.exception.type.ErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * 返回数据包装类
 *
 * @author marker
 * @param <T>
 */
@Setter
@Getter
public class R<T> {

    public static final String SUCCESSFUL_CODE = "S00";
    public static final String SUCCESSFUL_MESG = "操作成功";
    /**
     * 处理结果code
     */
    private String code;
    /**
     *
     * 处理结果描述信息
     */
    private String msg;
    /**
     * 请求结果生成时间戳
     */
    private Instant timestamp;

    /**
     * 处理结果数据信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public R() {
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     */
    public R(ErrorType errorType) {
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     * @param data
     */
    public R(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }


    public R(ErrorType errorType, String msg, T data) {
        this(errorType);
        this.data = data;
        this.msg = msg;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param msg
     * @param data
     */
    private R(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data 对象
     * @return Result
     */
    public static R success(Object data) {
        return new R<>(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static R success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static R fail() {
        return new R(SystemErrorType.CUSTOM_ERROR);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException 异常
     * @return Result
     */
    public static R fail(BaseException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param baseException 异常
     * @param data 对象
     * @return Result
     */
    public static R fail(BaseException baseException, Object data) {
        return new R<>(baseException.getErrorType(), data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @param data 对象
     * @return Result
     */
    public static R fail(ErrorType errorType, Object data) {
        return new R<>(errorType, data);
    }
    public static R fail(ErrorType errorType, String msg, Object data) {
        return new R<>(errorType, msg, data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @return Result
     */
    public static R fail(ErrorType errorType) {
        return R.fail(errorType, null);
    }



    /**
     * 获取转换后的SpringMVC错误
     * @param errors SpringMVC错误
     * @return
     */
    public static R fail(Errors errors) {
        List<FieldError> errorList = errors.getFieldErrors();
        Iterator<FieldError> it = errorList.iterator();
        FormFieldMessage[] errorCode = new FormFieldMessage[errorList.size()];
        int i = 0;
        while(it.hasNext()){
            FieldError oe = it.next();
            String field = oe.getField();
            errorCode[i++] = new FormFieldMessage(field, Integer.parseInt(oe.getCode()));
        }
        return new R(SystemErrorType.FormFieldError, errorCode);
    }


    /**
     * 系统异常类并返回结果数据
     *
     * @param data 对象
     * @return Result
     */
    public static R fail(Object data) {
        return new R<>(SystemErrorType.SYSTEM_ERROR, data);
    }


    public static R fail(String msg) {
        return new R<>(SystemErrorType.SYSTEM_ERROR, msg, null);
    }


    /**
     * 转换layui page
     * @param pageInfo
     * @return
     */
    public static LayPage layPage(Page<Role> pageInfo) {
        return new LayPage(pageInfo);
    }


    /**
     * 成功code=000000
     *
     * @return true/false
     */
    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    /**
     * 获取Map值
     * @param key key
     * @return
     */
    public String getDataMapAsString(String key) {
        if(this.data instanceof HashMap){
            return (String) ((HashMap) this.data).get(key);
        }
        return null;
    }

    /**
     * 获取Map值
     * @param key key
     * @return
     */
    public Long getDataMapAsLong(String key) {
        if(this.data instanceof HashMap){
            return (Long) ((HashMap) this.data).get(key);
        }
        return null;
    }
    /**
     * 获取Map值
     * @param key key
     * @return
     */
    public BigDecimal getDataMapAsBigDecimal(String key) {
        if(this.data instanceof HashMap){
            return new BigDecimal((String)((HashMap) this.data).get(key));
        }
        return null;
    }
}
