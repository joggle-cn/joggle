package com.wuweibi.bullet.builder;

import java.util.Map;

/**
 * Map参数构造器
 *
 * @author marker
 * @version 1.0
 * @date 2016/5/11 16:17
 * @update
 *      2016-05-27 marker 去掉原来的ThreadLocal方式
 */
public class MapBuilder {


    /** 多线程绑定共享变量 */
    private Map<String,Object> data = null;


    /**
     * 构造参数
     * @param size
     */
    private MapBuilder(int size){
        data = new java.util.HashMap<>(size);
    }



    /**
     * 创建一个Map构造器
     * @param size
     * @return
     */
    public static MapBuilder newMap(int size) {
        return new MapBuilder(size);
    }


    /**
     * 设置Map值
     * @param key
     * @param value
     * @return
     */
    public MapBuilder setParam(String key, Object value){
        data.put(key,value);
        return this;
    }



    /**
     * 构造Map
     * @return
     */
    public Map<String,Object> build() {
        return data;
    }
}
