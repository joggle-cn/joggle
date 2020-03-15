package com.wuweibi.bullet.exception.type;


/**
 * 错误类型接口
 *
 *
 * @author marker
 */
public interface ErrorType {
    /**
     * 返回code
     *
     * @return
     */
    String getCode();

    /**
     * 返回mesg
     *
     * @return
     */
    String getMsg();
}
