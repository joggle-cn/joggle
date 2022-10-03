package com.wuweibi.bullet.exception;

import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.domain.FieldMsg;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


/**
 * MVC控制器异常拦截处理
 * @author marker
 */
@Slf4j
@RestControllerAdvice
public class DefaultGlobalExceptionHandlerAdvice {

//    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
//    public Result missingServletRequestParameterException(MissingServletRequestParameterException ex) {
//        log.error("missing servlet request parameter exception:{}", ex.getMessage());
//        return Result.fail(SystemErrorType.ARGUMENT_NOT_VALID);
//    }

//    @ExceptionHandler(value = {MultipartException.class})
//    public Result uploadFileLimitException(MultipartException ex) {
//        log.error("upload file size limit:{}", ex.getMessage());
//        return Result.fail(SystemErrorType.UPLOAD_FILE_SIZE_LIMIT);
//    }
    @ExceptionHandler(value = {OAuth2Exception.class})
    public R OAuth2Exception(OAuth2Exception ex) {
        log.error("upload file size limit:{}", ex.getMessage());
        return R.fail(SystemErrorType.DEVICE_NOT_ONLINE);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R serviceException(MethodArgumentNotValidException ex) {
        return R.fail(SystemErrorType.ARGUMENT_NOT_VALID, ex.getBindingResult().getFieldError().getDefaultMessage(), null);
    }


    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public R service1Exception(MissingServletRequestParameterException ex) {
        log.warn("service exception:{}", ex.getMessage());
        return R.fail(SystemErrorType.ARGUMENT_NOT_VALID, ex.getMessage());
    }


    /**
     * 参数绑定异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(HttpStatus.OK)
    public R<FieldMsg> exception(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> list = bindingResult.getAllErrors();
        List<FieldMsg> dataList = new ArrayList(list.size());
        for (ObjectError error : list) {
            FieldError field = (FieldError) error;
            dataList.add(new FieldMsg(field.getField(), field.getDefaultMessage()));
        }
        return R.fail(SystemErrorType.SYSTEN_FORM_ERROR, dataList);
    }


    /**
     * 系统业务异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {BaseException.class})
    @ResponseStatus(HttpStatus.OK)
    public R baseException(BaseException ex) {
        log.info("service exception:{}", ex.getMessage());
        return ex.getResult();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R exception(Exception e) {
        log.error("base exception:{}", e);
        return R.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R throwable(Throwable e) {
        log.error("base exception:{}", e);
        return R.fail();
    }



    /**
     * 业务异常（不输出异常栈）
     * @param exception
     * @return R
     */
    @ExceptionHandler({ RException.class })
    @ResponseStatus(HttpStatus.OK)
    public R bindExceptionHandler(RException exception) {
        log.info("业务异常,ex = {}", exception.getMessage());
        return exception.getResult();
    }


}
