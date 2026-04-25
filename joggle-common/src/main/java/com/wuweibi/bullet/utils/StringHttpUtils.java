package com.wuweibi.bullet.utils;
/**
 * Created by marker on 2017/12/7.
 */

import org.apache.commons.lang3.ArrayUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author marker
 * @create 2017-12-07 下午1:01
 **/
public final class StringHttpUtils {

    public static String[] HTTP_ARRAY = new String[]{"http", "https"};
    /**
     * isHttp 是否未http协议 包含http和https
     * @param url
     * @return
     */
    public static boolean isHttp(String protocol) {
        if (ArrayUtils.contains(HTTP_ARRAY, protocol)) {
            return true;
        }
        return false;
    }



    /**
     * 获取二级域名
     * @param host 域名
     * @return
     */
    public static String getSecondLevelDomain(String host) {
        Pattern pattern = Pattern.compile("([0-9a-z\\.]+)\\.[0-9a-z]+\\.[0-9a-z]+");
        Matcher matcher = pattern.matcher(host);
        matcher.find();
        return matcher.group(1);
    }


}
