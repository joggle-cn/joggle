package com.wuweibi.bullet.entity.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wuweibi.bullet.exception.BaseException;
import com.wuweibi.bullet.exception.type.ErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import lombok.Getter;

import java.time.Instant;
import java.time.ZonedDateTime;


/**
 * 返回数据包装类
 *
 * @author marker
 * @param <T>
 */
@Getter
public class Result<T> {

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

    public Result() {
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     */
    public Result(ErrorType errorType) {
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
        this.timestamp = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     * @param data
     */
    public Result(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }


    public Result(ErrorType errorType, String msg, T data) {
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
    private Result(String code, String msg, T data) {
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
    public static Result success(Object data) {
        return new Result<>(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static Result fail() {
        return new Result(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException 异常
     * @return Result
     */
    public static Result fail(BaseException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param baseException 异常
     * @param data 对象
     * @return Result
     */
    public static Result fail(BaseException baseException, Object data) {
        return new Result<>(baseException.getErrorType(), data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @param data 对象
     * @return Result
     */
    public static Result fail(ErrorType errorType, Object data) {
        return new Result<>(errorType, data);
    }
    public static Result fail(ErrorType errorType, String msg, Object data) {
        return new Result<>(errorType, msg, data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @return Result
     */
    public static Result fail(ErrorType errorType) {
        return Result.fail(errorType, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data 对象
     * @return Result
     */
    public static Result fail(Object data) {
        return new Result<>(SystemErrorType.SYSTEM_ERROR, data);
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
}
