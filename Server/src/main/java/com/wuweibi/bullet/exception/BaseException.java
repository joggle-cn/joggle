package com.wuweibi.bullet.exception;

import com.wuweibi.bullet.exception.type.ErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import lombok.Getter;


/**
 * 基础自定义异常
 * @author marker
 */
@Getter
public class BaseException extends RuntimeException {
    /**
     * 异常对应的错误类型
     */
    private final ErrorType errorType;

    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public BaseException(ErrorType errorType) {
        super(errorType.getMsg());
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
}
