package com.wuweibi.bullet.utils;
/**
 * Created by marker on 2017/12/7.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author marker
 * @create 2017-12-07 下午1:01
 **/
public class StringHttpUtils {




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
