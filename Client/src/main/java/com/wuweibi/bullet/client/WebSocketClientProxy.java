package com.wuweibi.bullet.client;/**
 * Created by marker on 2017/11/20.
 */

/**
 * @author marker
 * @create 2017-11-20 下午1:10
 **/
public interface WebSocketClientProxy {


    /**
     * 发送byte数据
     *
     * @param results
     */
    void send(byte[] results);
}
