package com.wuweibi.bullet.controller.exception;

import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.exception.BaseException;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


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
    public Result OAuth2Exception(OAuth2Exception ex) {
        log.error("upload file size limit:{}", ex.getMessage());
        return Result.fail(SystemErrorType.DEVICE_NOT_ONLINE);
    }

//    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
//    public Result serviceException(MethodArgumentNotValidException ex) {
//        log.error("service exception:{}", ex.getMessage());
//        return Result.fail(SystemErrorType.ARGUMENT_NOT_VALID, ex.getBindingResult().getFieldError().getDefaultMessage());
//    }


//    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
//    public Result service1Exception(MissingServletRequestParameterException ex) {
//        log.error("service exception:{}", ex.getMessage());
//        return Result.fail(SystemErrorType.ARGUMENT_NOT_VALID, ex.getMessage());
//    }


    /**
     * 参数绑定异常处理
     * @param e
     * @return
     */
//    @ExceptionHandler(value = {BindException.class})
//    @ResponseStatus(HttpStatus.OK)
//    public Result exception(BindException e) {
//        BindingResult bindingResult = e.getBindingResult();
//        List<ObjectError> list = bindingResult.getAllErrors();
//        List<FieldMsg> dataList = new ArrayList<>(list.size());
//
//        for(ObjectError error : list){
//            FieldError field = (FieldError)error;
//            dataList.add(new FieldMsg(field.getField(), field.getDefaultMessage()));
//        }
//        return Result.fail(SystemErrorType.SYSTEN_FORM_ERROR, dataList);
//    }


    /**
     * 系统基础异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {BaseException.class})
    public Result baseException(BaseException ex) {
        log.error("base exception:{}", ex.getMessage());
        return Result.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(Exception e) {
        log.error("base exception:{}", e);
        return Result.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result throwable(Throwable e) {
        log.error("base exception:{}", e);
        return Result.fail();
    }


}