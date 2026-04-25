package com.wuweibi.bullet.common.lock.lock;

/**
 * Created by kl on 2017/12/29.
 */
public interface Lock {

    boolean acquire();

    boolean release();
}

