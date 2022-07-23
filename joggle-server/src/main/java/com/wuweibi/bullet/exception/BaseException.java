package com.wuweibi.bullet.exception;

import com.wuweibi.bullet.entity.api.R;
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
    private R result;


    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.result = R.fail(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 基础异常ErrorType转换
     * @param errorType
     */
    public BaseException(ErrorType errorType) {
        super(errorType.getMsg());
        this.result = R.fail(errorType);
    }

    /**
     * 基础异常ErrorType转换
     * @param errorType
     */
    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.result = R.fail(errorType);
    }

    /**
     * 基础异常ErrorType转换
     * @param errorType
     */
    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.result = R.fail(errorType);
    }


    public BaseException(R r) {
        super(r.getMsg());
        this.result = r;
    }

    public BaseException(String msg) {
        super(msg);
        this.result = R.fail(msg);
    }
}
