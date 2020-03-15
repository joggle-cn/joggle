package com.wuweibi.bullet.utils;/**
 * Created by marker on 2017/12/24.
 */

/**
 * @author marker
 * @create 2017-12-24 下午1:37
 **/
public class String2Utils {


    /**
     * 验证对象是否为null
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str){
        if(str == null)return true;
        if("".equals(str)){
            return true;
        }
        return false;
    }


}
