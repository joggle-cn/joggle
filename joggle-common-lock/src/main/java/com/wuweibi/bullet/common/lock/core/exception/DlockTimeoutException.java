package com.wuweibi.bullet.common.lock.core.exception;

public class DlockTimeoutException extends RuntimeException{
    public DlockTimeoutException(String errorMsg) {
        super(errorMsg);
    }
}
