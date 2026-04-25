package com.wuweibi.bullet.common.lock.handler.release;

import com.wuweibi.bullet.common.lock.model.LockInfo;

/**
 * 获取锁超时的处理逻辑接口
 *
 * @author wanglaomo
 * @since 2019/4/15
 **/
public interface ReleaseTimeoutHandler {

    void handle(LockInfo lockInfo);
}
