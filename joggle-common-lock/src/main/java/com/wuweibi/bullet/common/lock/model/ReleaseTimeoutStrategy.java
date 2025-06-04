package com.wuweibi.bullet.common.lock.model;

import com.wuweibi.bullet.common.lock.core.exception.DlockTimeoutException;
import com.wuweibi.bullet.common.lock.handler.release.ReleaseTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wanglaomo
 * @since 2019/4/15
 **/
public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler {

    /**
     * 继续执行业务逻辑，不做任何处理
     */
    NO_OPERATION() {
        @Override
        public void handle(LockInfo lockInfo) {
            // do nothing
        }
    },
    /**
     * 快速失败
     */
    FAIL_FAST() {
        private   final Logger log = LoggerFactory.getLogger(ReleaseTimeoutStrategy.class);
        @Override
        public void handle(LockInfo lockInfo) {
            String errorMsg = String.format("Found Lock(%s) already been released while lock lease time is %d s", lockInfo.getName(), lockInfo.getLeaseTime());
            log.error("{}", errorMsg);
            throw new DlockTimeoutException(errorMsg);
        }
    }
}
